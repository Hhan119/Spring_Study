package com.example.boot11.repository;

import java.util.List;

import org.springframework.ui.Model;

import com.example.boot11.dto.GalleryDto;

public interface GalleryDao {
	public List<GalleryDto> getList(GalleryDto dto);
	public GalleryDto getData(int num);
	public void insert(GalleryDto dto);
	public void delete(int num);
	public int getCount();
}
