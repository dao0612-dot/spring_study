package com.study.board.service;

import com.study.board.dto.BoardWriteRequest;
import com.study.board.exception.BlankValueException;
import com.study.board.exception.DBNotAffectedException;
import com.study.board.exception.NotFoundException;
import com.study.board.file.FileStore;
import com.study.board.file.StoredFile;
import com.study.board.mapper.BoardFileMapper;
import com.study.board.mapper.BoardMapper;
import com.study.board.vo.BoardFileVo;
import com.study.board.vo.BoardVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

            throw new BlankValueException("제목을 입력하세요.");
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

        BoardFileVo boardFileVo = new BoardFileVo();
        boardFileVo.setBoardIdx(boardVo.getBoardIdx());
        boardFileVo.setFileOriginalName(storedFile.originalName());
        boardFileVo.setFileSaveName(storedFile.saveName());
        boardFileVo.setFileSize(storedFile.size());

        if (boardFileMapper.insertBoardFile(boardFileVo) <= 0) {
            throw new DBNotAffectedException("파일 정보가 저장되지 않았습니다. 다시 시도해주세요.");
        }

    }


    // 게시글 자세히 보기
    public BoardVo getBoard(int boardIdx){
        BoardVo boardVo = boardMapper.selectBoard(boardIdx);
        if (boardVo == null){
            throw new NotFoundException("해당되는 게시글이 없습니다.");
        }
        return boardVo;
    }



    // 게시글 수정
    // 제목이 없으면 BlankValueException
    // DB에 변동 사항이 없으면 NotFoundException 즉 해당되는 게시글 못찾음
    public void updateBoard(int boardIdx, BoardWriteRequest req){

        String title = req.title();

        if(title==null || title.isBlank()){
            throw new BlankValueException("제목을 입력하세요.");
        }

        BoardVo boardVo = new BoardVo();
        boardVo.setBoardIdx(boardIdx);
        boardVo.setTitle(title);
        boardVo.setContent(req.content());
        // TODO: uptMemberIdx 1 임시값을 세션으로 적용
        boardVo.setUptMemberIdx(1);

        if (boardMapper.updateBoard(boardVo) <= 0){
            throw new NotFoundException("해당되는 게시글이 없습니다.");
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
            throw new NotFoundException("해당되는 게시글이 없습니다.");
        }

    }
}
