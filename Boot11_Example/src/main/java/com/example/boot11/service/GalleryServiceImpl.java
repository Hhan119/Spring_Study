package com.example.boot11.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.boot11.dto.GalleryDto;
import com.example.boot11.exception.NotOwnerException;
import com.example.boot11.repository.GalleryDao;

import lombok.extern.slf4j.Slf4j;


@Service
public class GalleryServiceImpl implements GalleryService {
	@Value("${file.location}")
	private String fileLocation;
	
	//한 페이지에 몇개씩 표시할 것인지
	final int PAGE_ROW_COUNT=8;
	//하단 페이지를 몇개씩 표시할 것인지
	final int PAGE_DISPLAY_COUNT=5;
	
	@Autowired
	private GalleryDao gallerydao;
	
	@Override
	public void saveImage(GalleryDto dto) {
		// 컨트롤러에서 전달한 GalleryDto에는 String caption과 MultipartFile image 정보만 들어 있다. 
		//1. 업로드된 파일 저장
		MultipartFile[] images = dto.getImages();
		for(MultipartFile tmp : images) {
			
			//저장할 파일의 이름 겹치지 않는 유일한 문자열로 얻어내기
			String saveFileName=UUID.randomUUID().toString();
			//저장할 파일의 전체 경로 구성하기
			String filePath=fileLocation+File.separator+saveFileName;
			try {
				//업로드된 파일을 이동시킬 목적지 File 객체
				File f=new File(filePath);
				//MultipartFile 객체의 메소드를 통해서 실제로 이동시키기(전송하기)
				tmp.transferTo(f);
			}catch(Exception e) {
				e.printStackTrace();
			}
			//2. 로그인된 사용자(userName) 읽어오기
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			//3. GalleryDto 에 추가 정보를 담고
			dto.setSaveFileName(saveFileName);
			dto.setWriter(userName);
			//4. DB 에 저장하기
			gallerydao.insert(dto);
			}
		}
	
	@Override
	public void getListImage(GalleryDto dto, Model model) {
		List<GalleryDto> list = gallerydao.getList(dto);
		//view page 에서 필요한 값을 Model 객체에 담기 
		model.addAttribute("list", list);
	}
	 
	@Override
	public void selectPage(Model model, int pageNum) {
		//보여줄 페이지의 시작 ROWNUM
		int startRowNum = 1 + (pageNum-1) * PAGE_ROW_COUNT;
		//보여줄 페이지의 끝 ROWNUM
		int endRowNum = pageNum * PAGE_ROW_COUNT;
		
		//startRowNum 과 endRowNum  을 GalleryDto 객체에 담고
		GalleryDto dto = new GalleryDto();
		dto.setStartRowNum(startRowNum);
		dto.setEndRowNum(endRowNum);
		
		//GalleryDao 객체를 이용해서 회원 목록을 얻어온다.
		List<GalleryDto> list = gallerydao.getList(dto);
	   
		//하단 시작 페이지 번호 
		int startPageNum = 1 + ((pageNum-1)/PAGE_DISPLAY_COUNT) * PAGE_DISPLAY_COUNT;
		//하단 끝 페이지 번호
		int endPageNum = startPageNum + PAGE_DISPLAY_COUNT - 1;
	   
		//전체 row 의 갯수
		int totalRow = gallerydao.getCount();
		//전체 페이지의 갯수 구하기
		int totalPageCount = (int)Math.ceil(totalRow / (double)PAGE_ROW_COUNT);
		//끝 페이지 번호가 이미 전체 페이지 갯수보다 크게 계산되었다면 잘못된 값이다.
		if(endPageNum > totalPageCount){
			endPageNum = totalPageCount; //보정해 준다. 
		}
		
		//view page 에서 필요한 값을 Model 객체에 담기 
		model.addAttribute("list", list);
		model.addAttribute("startPageNum", startPageNum);
		model.addAttribute("endPageNum", endPageNum);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("pageNum", pageNum);
	}
	
	@Override
	public void deleteImage(int num) {
		GalleryDto dto = gallerydao.getData(num);
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		if(!dto.getWriter().equals(userName)) {
			throw new NotOwnerException("남의 파일 지우면 혼난다.");
		}
		gallerydao.delete(num);
	}

	@Override
	public void getDataImage(int num, Model model) {
		GalleryDto dto=gallerydao.getData(num);
		model.addAttribute("dto", dto);
		System.out.println(dto);
	}

}
