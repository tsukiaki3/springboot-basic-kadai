package com.example.postingapp.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.postingapp.entity.Role;
import com.example.postingapp.entity.User;
import com.example.postingapp.form.SignupForm;
import com.example.postingapp.repository.RoleRepository;
import com.example.postingapp.repository.UserRepository;

@Service
public class UserService {
	
	//定数
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    //DI
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //@Transactionalアノテーションをつけることで、そのメソッドをトランザクション化
    //データベース操作が完全に成功するか、すべて失敗するかのどちらかに白黒はっきり
    @Transactional
    public User createUser(SignupForm signupForm) {
    	
    	//★重要Userエンティティをインスタンス化
        User user = new User();
        
        //
        Role role = roleRepository.findByName("ROLE_GENERAL");

        user.setName(signupForm.getName());
        user.setEmail(signupForm.getEmail());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        user.setRole(role);
        //バリデーションエラーが発生しなければ、enableカラムの値がfalse
        user.setEnabled(false);

        return userRepository.save(user);
    }
    
    // メールアドレスが登録済みかどうかをチェック
    public boolean isEmailRegistered(String email) {
    	
    	//メールアドレスで検索
        User user = userRepository.findByEmail(email);
        
        //メールアドレスが登録済みであればtrueを返す
        return user != null;
    }    
    
    // パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }
    
    // ユーザーを有効にする
    @Transactional
    public void enableUser(User user) {
    	//一致するデータが見つかれば、そのデータに紐づいたユーザーのenableカラムの値をtrue
        user.setEnabled(true);
        userRepository.save(user);
    }    
}