package com.study.board.service;

import com.study.board.dto.BoardWriteRequest;
import com.study.board.exception.BlankValueException;
import com.study.board.exception.DBNotAffectedException;
import com.study.board.exception.NotFoundException;
import com.study.board.mapper.BoardMapper;
import com.study.board.vo.BoardVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardMapper boardMapper;

    public BoardService(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    // 삭제글 제외한 게시글 목록 조회
    public List<BoardVo> getBoardList(){
        return boardMapper.selectBoardList();
    }



    // 게시글 저장
    // 제목이 없으면 BlankValueException
    // DB에 변동 사항이 없으면 DBNotAffectedException
    public void saveBoard(BoardWriteRequest req){

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

    public void delBoard(int boardIdx){

    }
}
