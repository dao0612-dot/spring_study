package com.study.skillup.board.service;

import com.study.skillup.board.vo.BoardVo;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

// 게시판 엑셀: 목록 -> .xlsx 만들기
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
}
