package com.study.skillup.board.service;

import com.study.skillup.board.dto.BoardWriteRequest;
import com.study.skillup.board.vo.BoardVo;
import com.study.skillup.common.exception.ErrorCode;
import com.study.skillup.common.exception.InvalidValueException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// 게시판 엑셀: 목록 <-> .xlsx (업로드 / 다운로드)
@Service
public class BoardExcelService {
    private static final String[] HEADERS = {"번호", "제목", "내용", "작성자 번호", "작성일"};
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final BoardService boardService;
    public BoardExcelService(BoardService boardService) {
        this.boardService = boardService;
    }

    // 목록을 엑셀 파일 바이트로
    public byte[] createBoardListExcel() {
        List<BoardVo> boards = boardService.getBoardList();

        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("게시글");

            // 1행: 머리
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                headerRow.createCell(i).setCellValue(HEADERS[i]);
            }

            // 2행부터: 글 하나 = 한 줄
            int rowIdx = 1;
            for (BoardVo board : boards) {
                XSSFRow row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(board.getBoardIdx());
                row.createCell(1).setCellValue(board.getTitle());
                row.createCell(2).setCellValue(board.getContent());
                row.createCell(3).setCellValue(board.getMemberIdx());
                row.createCell(4).setCellValue(board.getRegDttm().format(DATE_FORMAT));
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("엑셀 만들기 실패", e);
        }
    }

    // 엑셀 파일을 목록으로
    @Transactional
    public int uploadBoardExcel(MultipartFile file, int memberIdx) {
        String originalName = file.getOriginalFilename();

        if (originalName == null || !originalName.toLowerCase().endsWith(".xlsx")) {
            throw new InvalidValueException(ErrorCode.FILE_TYPE_NOT_ALLOWED, "엑셀 업로드: 형식 불일치 name=" + originalName);
        }

        List<BoardWriteRequest> requests = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                String title = formatter.formatCellValue(row.getCell(0));
                String content = formatter.formatCellValue(row.getCell(1));
                if (title.isBlank() && content.isBlank()) {
                    continue;
                }

                requests.add(new BoardWriteRequest(title, content));
            }
        } catch (NotOfficeXmlFileException | IOException e) {
            throw new InvalidValueException(ErrorCode.FILE_TYPE_NOT_ALLOWED, "엑셀 업로드: 파일 읽기 실패 name=" + originalName + "원인= " + e.getMessage());
        }

        if (requests.isEmpty() || requests.size() > 100) {
            throw new InvalidValueException(ErrorCode.Excel_OUT_OF_RANGE, "엑셀 업로드: 줄 수 범위 밖 rows=" + requests.size());
        }

        for (BoardWriteRequest req : requests) {
            boardService.saveBoard(req, null, memberIdx);
        }

        return requests.size();
    }
}
