package com.example.springtutorial.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

//ログ
@Slf4j

//フィルターの実装には Filterインターフェース を使う
public class HeaderFooterFilter implements Filter {
    @Override
    //アプリ起動時に初期化処理(フィルターを生成したときに呼ばれるメソッド)
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初期化処理
        log.info("init()：フィルターを初期化します。");
    }

    @Override
    //リクエスト・レスポンスの前処理と後処理（引数にリクエスト情報、レスポンス情報がある）
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        log.info("doFilter()：フィルター処理を開始します。");

        //メソッドの引数がデータ取得に適していないため型変換する
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // リクエスト・レスポンスの設定（文字化け対策）
        httpResponse.setContentType("text/html; charset=UTF-8");

        // ログインユーザー名を取得
        String username = httpRequest.getRemoteUser();

        // ログインユーザー名に応じてメッセージを切り替える
        if (username == null) {
            // 未ログインの場合
            httpResponse.getWriter().write("はじめまして、ゲストさん。");
        } else {
            httpResponse.getWriter().write("こんにちは、" + username + "さん");
        }

        // 別のフィルターを呼び出す(chain.doFilter()メソッドの手前に前処理、後ろに後処理を記述しましょう)
        chain.doFilter(request, response);

        // 後処理
        //フッター処理
        httpResponse.getWriter().write("<p>&copy; spring_tutorial All rights reserved.</p>");
        log.info("doFilter()：フィルター処理が完了しました。");
    }

    @Override
    //アプリ停止時の終了処理（フィルターが放棄されるときに呼ばれるメソッド）
    public void destroy() {
        // 破棄時の終了処理
        log.info("destroy()：フィルターを破棄します。");
    }
}
