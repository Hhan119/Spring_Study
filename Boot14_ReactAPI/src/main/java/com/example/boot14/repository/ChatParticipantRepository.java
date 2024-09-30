package com.example.boot14.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.boot14.entity.ChatParticipant;



@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {


	




}
