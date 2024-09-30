package com.example.boot13.service;

import org.springframework.ui.Model;

import com.example.boot13.dto.CookDto;

public interface CookService {
	public void getList(Model model);
	public void getData(Long num, Model model);
	public void save(CookDto dto);
	public void delete(Long num);
	public void update(CookDto dto);
}
