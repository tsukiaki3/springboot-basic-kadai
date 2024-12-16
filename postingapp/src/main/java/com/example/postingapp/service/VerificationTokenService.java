package com.example.postingapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.postingapp.entity.User;
import com.example.postingapp.entity.VerificationToken;
import com.example.postingapp.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {
	//リポジトリ定数
    private final VerificationTokenRepository verificationTokenRepository;

    //DI
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Transactional
    public void create(User user, String token) {
    	//インスタンス生成
        VerificationToken verificationToken = new VerificationToken();

        verificationToken.setUser(user);
        verificationToken.setToken(token);

        //保存
        verificationTokenRepository.save(verificationToken);
    }

    // トークンの文字列で検索した結果を返す
    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}