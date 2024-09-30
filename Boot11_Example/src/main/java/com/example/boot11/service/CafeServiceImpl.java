package com.example.boot11.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.boot11.dto.CafeCommentDto;
import com.example.boot11.dto.CafeDto;
import com.example.boot11.exception.NotOwnerException;
import com.example.boot11.repository.CafeCommentDao;
import com.example.boot11.repository.CafeDao;

@Service
public class CafeServiceImpl implements CafeService {
	// 한 페이지에 글을 몇개씩 표시할 것인지
	final int PAGE_ROW_COUNT=5;
	// 하단 페이지 UI를 몇개씩 표시 할 것인지
	final int PAGE_DISPLAY_COUNT=5;
	
	@Autowired
	private CafeDao cafedao;
	@Autowired
	private CafeCommentDao commentDao;
	
	@Override
	public void getList(CafeDto dto, Model model) {
		// pageNum 에 해당하는 글정보를 select 에서 Model 객체에 담는 작업을 하면 된다.
		
		int pageNum=dto.getPageNum();
		//보여줄 페이지의 시작 ROWNUM
		int startRowNum=1+(pageNum-1)*PAGE_ROW_COUNT;
		//보여줄 페이지의 끝 ROWNUM
		int endRowNum=pageNum*PAGE_ROW_COUNT;
		
		//하단 시작 페이지 번호 
		int startPageNum = 1 + ((pageNum-1)/PAGE_DISPLAY_COUNT)*PAGE_DISPLAY_COUNT;
		//하단 끝 페이지 번호
		int endPageNum=startPageNum+PAGE_DISPLAY_COUNT-1;
		//전체 글의 갯수
		int totalRow=cafedao.getCount(dto);
		//전체 페이지의 갯수 구하기
		int totalPageCount=(int)Math.ceil(totalRow/(double)PAGE_ROW_COUNT);
		//끝 페이지 번호가 이미 전체 페이지 갯수보다 크게 계산되었다면 잘못된 값이다.
		if(endPageNum > totalPageCount){
			endPageNum=totalPageCount; //보정해 준다. 
		}
		
		//위에서 계산된 startRowNum 과 endRowNum 을 dto 담고
		dto.setStartRowNum(startRowNum);
		dto.setEndRowNum(endRowNum);
		
		//CafeDto 를 인자로 전달해서 글목록 얻어오기
		List<CafeDto> list=cafedao.getList(dto);
		
		// view page 에 전달할 내용을 Model 객체에 담는다. 
		model.addAttribute("list", list);
		model.addAttribute("startPageNum", startPageNum);
		model.addAttribute("endPageNum", endPageNum);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("dto", dto); //키워드정보가 들어 있는 dto 를 모델에 담기 
		model.addAttribute("totalRow", totalRow);
		cafedao.getList(dto);
	}
	
	// dto에는 title, writer이 담겨져있다. 
	@Override
	public void saveContent(CafeDto dto) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		dto.setWriter(userName);
		cafedao.insert(dto);
	}

	@Override
	public void getDetail(CafeDto dto, Model model) {
		// 이전글의 글번호 다음글의 글번호도 받아 올수 있도록 getDetail()메소드를 호출한다.
		CafeDto resultDto=cafedao.getDetail(dto);
		// 원래의 검색 조건을 글정보가 들어 있는 결과 dto에 추가해준다. 
		resultDto.setCondition(dto.getCondition());
		resultDto.setKeyword(dto.getKeyword());
		
		// 댓글 목록을 얻어온다.
		// CafeDto에 ref_group 번호를 담아서 dao에 전달해서 댓글 목록을 얻어낸다.
		CafeCommentDto commentDto = new CafeCommentDto();
		//원글의 글번호를 담아서 
		commentDto.setRef_group(dto.getNum());
		//댓글의 페이지 번호
		int pageNum=1;
		/*
			[ 댓글 페이징 처리에 관련된 로직 ]
		*/
		//한 페이지에 댓글을 몇개씩 표시할 것인지
		final int PAGE_ROW_COUNT=10;

		//보여줄 페이지의 시작 ROWNUM
		int startRowNum=1+(pageNum-1)*PAGE_ROW_COUNT;
		//보여줄 페이지의 끝 ROWNUM
		int endRowNum=pageNum*PAGE_ROW_COUNT;
		//계산된 값을 dto 에 담는다
		commentDto.setStartRowNum(startRowNum);
		commentDto.setEndRowNum(endRowNum);
		
		//원글에 달린 댓글 목록 얻어내기 
		List<CafeCommentDto> commentList=commentDao.getList(commentDto);
				
		//원글의 글번호를 이용해서 댓글 전체의 갯수를 얻어낸다.
		int totalRow=commentDao.getCount(dto.getNum());
		//댓글 전체 페이지의 갯수
		int totalPageCount=(int)Math.ceil(totalRow/(double)PAGE_ROW_COUNT);	
		
		model.addAttribute("dto", resultDto);
		model.addAttribute("commentList", commentList);
		model.addAttribute("totalPageCount", totalPageCount);
	}

	@Override
	public void deleteContent(int num) {
		// 글 작성자와 
		String writer = cafedao.getData(num).getWriter();
		// 로그인된 사용자와 같은 경우에만 삭제
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		// 만약에 일치하지 않으면 예외(NotOwnerExceptin) 발생시키기
		if(!writer.equals(userName)) {
			throw new NotOwnerException("삭제할 권한이 없습니다!");
		}
		// DB에서 num에 해당하는 글 삭제하기 
		cafedao.delete(num);
	}

	@Override
	public void getData(Model model, int num) {
		// num을 이용해서 글정보를 얻어와서 
		CafeDto dto = cafedao.getData(num);
		// Model 객체에 담는다.
		model.addAttribute("dto", dto);
	}

	@Override
	public void updateContent(CafeDto dto) {
		// 글 작성자와 
		String writer = cafedao.getData(dto.getNum()).getWriter();
		// 로그인된 사용자와 같은 경우에만 삭제
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		// 만약에 일치하지 않으면 예외(NotOwnerExceptin) 발생시키기
		if(!writer.equals(userName)) {
			throw new NotOwnerException("수정 권한이 없습니다!");
		}
		// DB에서 글 수정하기
		cafedao.update(dto);
	}

	@Override
	public void saveComment(CafeCommentDto dto) {
		// 댓글 작성자는 SpringSecurity로 부터 얻어내기
		String writer = SecurityContextHolder.getContext().getAuthentication().getName();
		// 글 번호를 미리 얻어낸다.
		int num = commentDao.getSequence();
		dto.setWriter(writer);
		dto.setNum(num);
		// 만약에 comment_group 번호가 넘어오지 않으면 원글의 댓글이다.
		if(dto.getComment_group() == 0 ) {
			// 원글의 댓글인 경우 댓글의 번호(num)가 곧 comment_group 번호가 된다.
			dto.setComment_group(num);
		}
		// DB에 저장하기
		commentDao.insert(dto);
	}

	@Override
	public void deleteComment(int num) {
		// 글 작성자를 얻어낸다.
		String writer= commentDao.getData(num).getWriter();
		// 댓글 작성자는 SpringSecurity로 부터 얻어내기
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		// 만약에 일치하지 않으면 예외(NotOwnerException) 발생시키기
		if(!writer.equals(userName)) {
			throw new NotOwnerException("금지된 요청입니다.");
		}
		// 실제로 삭제하기 
		commentDao.delete(num);
	}

	@Override
	public void updateComment(CafeCommentDto dto) {
		// 댓글 작성자는 SpringSecurity로 부터 얻어내기
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		// 글 작성자를 얻어낸다.
		String writer= commentDao.getData(dto.getNum()).getWriter();
		// 만약에 일치하지 않으면 예외(NotOwnerException) 발생시키기
		if(!userName.equals(writer)) {
			throw new NotOwnerException("삭제 할 권한이 없습니다.");
		}
		commentDao.update(dto);
	}

	@Override
	public void getCommentList(Model model, CafeCommentDto dto) {
		// 요청된 댓글의 페이지 번호
		int pageNum = dto.getPageNum();
	/*
		[ 댓글 페이징 처리에 관련된 로직 ]
	*/
	//한 페이지에 댓글을 몇개씩 표시할 것인지
	final int PAGE_ROW_COUNT=10;

	//보여줄 페이지의 시작 ROWNUM
	int startRowNum=1+(pageNum-1)*PAGE_ROW_COUNT;
	//보여줄 페이지의 끝 ROWNUM
	int endRowNum=pageNum*PAGE_ROW_COUNT;
	//계산된 값을 dto 에 담는다
	dto.setStartRowNum(startRowNum);
	dto.setEndRowNum(endRowNum);
	
	//pageNum에 해당하는 댓글 목록만 select 되도록 한다. 
	List<CafeCommentDto> commentList=commentDao.getList(dto);
	
	//응답에 필요한 데이터를 Model 객체에 담는다.
	model.addAttribute("commentList", commentList); //댓글 목록
	model.addAttribute("pageNum", pageNum);  //페이지번호 
	model.addAttribute("ref_group", dto.getRef_group()); //원글의 글번호
	}

}
