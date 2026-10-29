package com.study.skillup.board.service;

import com.study.skillup.board.dto.BoardWriteRequest;
import com.study.skillup.common.exception.BlankValueException;
import com.study.skillup.common.exception.DBNotAffectedException;
import com.study.skillup.common.exception.ErrorMessage;
import com.study.skillup.common.exception.NotFoundException;
import com.study.skillup.common.file.DownloadFile;
import com.study.skillup.common.file.FileStore;
import com.study.skillup.common.file.StoredFile;
import com.study.skillup.board.mapper.BoardFileMapper;
import com.study.skillup.board.mapper.BoardMapper;
import com.study.skillup.board.vo.BoardFileVo;
import com.study.skillup.board.vo.BoardVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class BoardService {

    private final BoardMapper boardMapper;
    private final BoardFileMapper boardFileMapper;
    private final FileStore fileStore;

    public BoardService(BoardMapper boardMapper, BoardFileMapper boardFileMapper, FileStore fileStore) {
        this.boardMapper = boardMapper;
        this.boardFileMapper = boardFileMapper;
        this.fileStore = fileStore;
    }


    // 삭제글 제외한 게시글 목록 조회
    public List<BoardVo> getBoardList(){
        return boardMapper.selectBoardList();
    }



    // 게시글 저장
    // 제목이 없으면 BlankValueException
    // DB에 변동 사항이 없으면 DBNotAffectedException
    @Transactional
    public void saveBoard(BoardWriteRequest req, MultipartFile file){

        String title = req.title();

        if (title == null || title.isBlank()){

            throw new BlankValueException(ErrorMessage.BOARD_TITLE_BLANK);
        }

        BoardVo boardVo = new BoardVo();

        boardVo.setTitle(title);
        boardVo.setContent(req.content());
        // TODO: memberIdx: 1 임시값을 세션으로 적용
        boardVo.setMemberIdx(1);


        // 0 이하면 예외, 비정상
        if (boardMapper.insertBoard(boardVo) <= 0) {

            throw new DBNotAffectedException("저장된 내용이 없습니다. 다시 시도해주세요.");
        }

        if (file == null || file.isEmpty()){
            return;
        }


        StoredFile storedFile = fileStore.store(file);

        try {
            BoardFileVo boardFileVo = new BoardFileVo();
            boardFileVo.setBoardIdx(boardVo.getBoardIdx());
            boardFileVo.setFileOriginalName(storedFile.originalName());
            boardFileVo.setFileSaveName(storedFile.saveName());
            boardFileVo.setFileSize(storedFile.size());

            if (boardFileMapper.insertBoardFile(boardFileVo) <= 0) {
                throw new DBNotAffectedException("파일 정보가 저장되지 않았습니다. 다시 시도해주세요.");
            }
        } catch (RuntimeException e) {
            fileStore.delete(storedFile.saveName());
            throw e;
        }
    }


    // 게시글 자세히 보기
    public BoardVo getBoard(int boardIdx){
        BoardVo boardVo = boardMapper.selectBoard(boardIdx);
        if (boardVo == null){
            throw new NotFoundException(ErrorMessage.BOARD_NOT_FOUND);
        }
        return boardVo;
    }



    // 게시글 수정
    // 제목이 없으면 BlankValueException
    // DB에 변동 사항이 없으면 NotFoundException 즉 해당되는 게시글 못찾음
    public void updateBoard(int boardIdx, BoardWriteRequest req){

        String title = req.title();

        if(title==null || title.isBlank()){
            throw new BlankValueException(ErrorMessage.BOARD_TITLE_BLANK);
        }

        BoardVo boardVo = new BoardVo();
        boardVo.setBoardIdx(boardIdx);
        boardVo.setTitle(title);
        boardVo.setContent(req.content());
        // TODO: uptMemberIdx 1 임시값을 세션으로 적용
        boardVo.setUptMemberIdx(1);

        if (boardMapper.updateBoard(boardVo) <= 0){
            throw new NotFoundException(ErrorMessage.BOARD_NOT_FOUND);
        }

    }

    // 게시글 삭제
    // DB에 변동 사항이 없으면 NotFoundException 즉 해당되는 게시글 못찾음
    public void deleteBoard(int boardIdx){

        BoardVo boardVo = new BoardVo();
        boardVo.setBoardIdx(boardIdx);
        // TODO: delMemberIdx 1 임시값을 세션으로 적용
        boardVo.setDelMemberIdx(1);

        if (boardMapper.deleteBoard(boardVo) <= 0) {
            throw new NotFoundException(ErrorMessage.BOARD_NOT_FOUND);
        }

    }

    // 파일 리스트
    public List<BoardFileVo> getBoardFileList(int boardIdx){
        return boardFileMapper.selectBoardFileList(boardIdx);
    }

    // 내려받기용 파일(원래 이름 + 디스크 파일) 조회 | 파일 정보가 없거나 디스크에 없으면 NotFoundException
    public DownloadFile getBoardFile(int fileIdx){

        BoardFileVo boardFileVo = boardFileMapper.selectBoardFile(fileIdx);
        if (boardFileVo == null) {
            throw new NotFoundException(ErrorMessage.BOARD_FILE_NOT_FOUND);
        }
        Resource resource = fileStore.load(boardFileVo.getFileSaveName());
        if (resource == null) {
            log.error("DB와 매칭되는 파일이 저장소에 없거나 읽을 수 없는 파일: fileIdx={}, saveName={}", fileIdx, boardFileVo.getFileSaveName());
            throw new NotFoundException(ErrorMessage.BOARD_FILE_NOT_FOUND);
        }

        return new DownloadFile(boardFileVo.getFileOriginalName(), resource);
    }
}
