package com.example.boot14.repository;

import java.util.List;

import com.example.boot14.dto.GalleryDto;

public interface GalleryDao {
	public List<GalleryDto> getList(GalleryDto dto);
	public GalleryDto getData(int num);
	public void insert(GalleryDto dto);
	public void delete(int num);
	public int getCount();
}
