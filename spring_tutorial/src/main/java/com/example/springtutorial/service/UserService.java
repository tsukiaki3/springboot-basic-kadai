package com.example.springtutorial.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springtutorial.entity.User;
import com.example.springtutorial.repository.UserRepository;

//サービスとして認識させる
@Service
public class UserService {
	
	//リポジトリ定数
	private final UserRepository userRepository;

	
	
    // 依存性の注入（DI）.コンストラクタが複数あるときは、依存性を注入したいものに@Autowiredアノテーションが必要
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ユーザーの登録メソッド
    public void createUser(String userName, String password, int roleId) {
        // ユーザー名の未入力チェック
        if (userName == null || userName.isEmpty()) {
        	//例外をthrow
            throw new IllegalArgumentException("ユーザー名を入力してください。");
        }

        // ユーザー名の重複チェック
        if (!userRepository.findByUserName(userName).isEmpty()) {
        	//例外をthrow
            throw new IllegalArgumentException("そのユーザー名は既に使用されています。");
        }

        // ユーザー登録用のエンティティ作成
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setRoleId(roleId);

        // DBに登録
        userRepository.save(user);
    }

    //ユーザー(usersテーブル)を取得するメソッド
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
  
}
