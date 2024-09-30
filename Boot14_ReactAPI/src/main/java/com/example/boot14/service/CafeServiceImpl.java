package com.example.boot14.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.boot14.dto.CafeCommentDto;
import com.example.boot14.dto.CafeDto;
import com.example.boot14.repository.CafeCommentDao;
import com.example.boot14.repository.CafeDao;

@Service
public class CafeServiceImpl implements CafeService {
	// 한 페이지에 글을 몇개씩 표시 할 것인지
	final int PAGE_ROW_COUNT = 5;
	// 하단 페이지 UI를 몇개씩 표시할 것인지
	final int PAGE_DISPLAY_COUNT = 5;

	@Autowired
	private CafeDao cafeDao;
	@Autowired
	private CafeCommentDao cafeCommentDao;

	@Override
	public Map<String, Object> getList(CafeDto dto) {
		// CafeDto 로 부터 페이지 번호를 얻어낸다
		int pageNum = dto.getPageNum();
		// 보여줄 페이지의 시작 ROWNUM
		int startRowNum = 1 + (pageNum - 1) * PAGE_ROW_COUNT;
		// 보여줄 페이지의 끝 ROWNUM
		int endRowNum = pageNum * PAGE_ROW_COUNT;

		// 하단 시작 페이지 번호
		int startPageNum = 1 + ((pageNum - 1) / PAGE_DISPLAY_COUNT) * PAGE_DISPLAY_COUNT;
		// 하단 끝 페이지 번호
		int endPageNum = startPageNum + PAGE_DISPLAY_COUNT - 1;

		// 전체 글의 갯수
		int totalRow = cafeDao.getCount(dto);
		// 전체 페이지의 갯수 구하기
		int totalPageCount = (int) Math.ceil(totalRow / (double) PAGE_ROW_COUNT);
		// 끝 페이지 번호가 이미 전체 페이지 갯수보다 크게 계산되었다면 잘못된 값이다.
		if (endPageNum > totalPageCount) {
			endPageNum = totalPageCount; // 보정해 준다.
		}
		// 위에서 계산된 startRowNum과 endRowNum을 dto에 담고
		dto.setStartRowNum(startRowNum);
		dto.setEndRowNum(endRowNum);
		// CafeDto를 인자로 전달해서 글 목록 가져오기
		List<CafeDto> list = cafeDao.getList(dto);

		// 글 목록과 페이징 처리에 관련된 정보를 Map에 담아서 리턴해준다.
		return Map.of("list", list, 
				"startPageNum", startPageNum, 
				"endPageNum", endPageNum,
				"totalPageCount", 	totalPageCount, 
				"pageNum", pageNum, 
				"totalRow", totalRow);
	}

	@Override
	public void saveContent(CafeDto dto) {
		// 글 작성자는 spring security 에서 1회성 인증을 받은 userName 이다
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		// 글작성자를 dto 에 담고
		dto.setWriter(userName);
		// DB 에 저장
		cafeDao.insert(dto);
	}

	@Override
	public Map<String, Object> getDetail(CafeDto dto) {

		//글번호를 이용해서 글 하나의 정보를 얻어와서 
		CafeDto resultDto=cafeDao.getDetail(dto);
		//원래의 검색 조건을 글정보가 들어 있는 결과 dto 에 추가해준다. 
		resultDto.setCondition(dto.getCondition());
		resultDto.setKeyword(dto.getKeyword());
		
		//CafeDto 에 ref_group 번호를 담아서 dao 에 전달해서 댓글 목록을 얻어낸다
		CafeCommentDto commentDto=new CafeCommentDto();
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
		List<CafeCommentDto> commentList=cafeCommentDao.getList(commentDto);
		
		//원글의 글번호를 이용해서 댓글 전체의 갯수를 얻어낸다.
		int totalRow=cafeCommentDao.getCount(dto.getNum());
		//댓글 전체 페이지의 갯수
		int totalPageCount=(int)Math.ceil(totalRow/(double)PAGE_ROW_COUNT);
		
		return Map.of("dto", resultDto,
				"commentList", commentList,
				"totalPageCount", totalPageCount);
	}

	@Override
	public void deleteContent(int num) {
		cafeDao.delete(num);

	}

	@Override
	public CafeDto getData(int num) {
		return cafeDao.getData(num);
	}

	@Override
	public void updateContent(CafeDto dto) {
		cafeDao.update(dto);

	}

	@Override
	public CafeCommentDto saveComment(CafeCommentDto dto) {
		// 댓글 작성자는 SpringSecurity로부터 얻어오기
		String writer = SecurityContextHolder.getContext().getAuthentication().getName();
		// 글번호를 미리 얻어낸다.
		int num = cafeCommentDao.getSequence();
		dto.setWriter(writer);
		dto.setNum(num);
		// 만약에 comment_group 번호가 넘어오지 않으면 원글의 댓글이다.
		if (dto.getComment_group() == 0) {
			// 원글의 댓글인 경우 댓글의 번호(num)가 곧 comment_group 번호가 된다.
			dto.setComment_group(num);
		}
		// DB에 저장하기
		cafeCommentDao.insert(dto);
		// 지금 저장한 댓글 정보를 리턴해준다.
		return cafeCommentDao.getData(num);
	}

	@Override
	public Map<String, Object> getCommentList(CafeCommentDto dto) {
		//요청된 댓글의 페이지 번호
		int pageNum=dto.getPageNum();
		/*
		 * [ 댓글 페이징 처리에 관련된 로직 ]
		 */
		// 한 페이지에 댓글을 몇개씩 표시할 것인지
		final int PAGE_ROW_COUNT = 10;

		// 보여줄 페이지의 시작 ROWNUM
		int startRowNum = 1 + (pageNum - 1) * PAGE_ROW_COUNT;
		// 보여줄 페이지의 끝 ROWNUM
		int endRowNum = pageNum * PAGE_ROW_COUNT;
		// 계산된 값을 dto 에 담는다
		dto.setStartRowNum(startRowNum);
		dto.setEndRowNum(endRowNum);
		
		// pageNum에 해당하는 댓글 목록 얻어오기
		List<CafeCommentDto> commentList=cafeCommentDao.getList(dto);
		// 원글의 글번호를 이용해서 댓글 전체의 갯수를 얻어낸다.
		int totalRow=cafeCommentDao.getCount(dto.getRef_group());
		// 댓글 전체 페이지의 갯수
		int totalPageCount=(int)Math.ceil(totalRow/(double)PAGE_ROW_COUNT);
		
		// 댓글 목록과 전체 페이지의 갯수를 Map에 담아서 리턴한다.
		return Map.of("commentList", commentList, "totalPageCount", totalPageCount);
	}

	@Override
	public void deleteComment(int num) {
		cafeCommentDao.delete(num);

	}

	@Override
	public void updateComment(CafeCommentDto dto) {
		cafeCommentDao.update(dto);

	}

}
