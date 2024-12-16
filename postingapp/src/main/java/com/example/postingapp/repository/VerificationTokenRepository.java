package com.example.postingapp.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.postingapp.entity.VerificationToken;
public interface VerificationTokenRepository extends JpaRepository< VerificationToken, Integer> {
	
	//トークンに一致するデータをverification_tokensテーブルから探す
    public VerificationToken findByToken(String token);
}