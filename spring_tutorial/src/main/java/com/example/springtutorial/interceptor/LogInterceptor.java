package com.example.springtutorial.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

//ロガー生成
@Slf4j

//インターセプターの実装にはHandlerInterceptorインターフェース を使う
public class LogInterceptor implements HandlerInterceptor {
    @Override
    // リクエストの前処理(引数はリクエスト情報のオブジェクト,レスポンス情報のオブジェクト)
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.info("preHandle()：リクエスト：{} メソッド：{}",
        		
        		//リクエスト先のURL取得
                request.getRequestURL(),
                request.getMethod());

        // 戻り値をtrueとするとコントローラが実行。trueを返すことでコントローラを呼びリクエスト（コントローラーに通す）
        return true;
    }


    @Override
    //異常終了するとposthandleメソッドは呼ばれない
    // レスポンスの後処理（ビュー表示前）(引数はリクエスト情報のオブジェクト,レスポンス情報のオブジェクト)
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

        log.info("postHandle()：ステータスコード{}",
        		
        		//レスポンスが持つHTTPのステータスコード取得
                response.getStatus());
    }


    @Override
    //異常終了してもafterCompletionは実行される
    // ビュー表示後の完了処理(引数はリクエスト情報のオブジェクト,レスポンス情報のオブジェクト)
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        log.info("afterCompletion()：コンテンツタイプ：{}",
        		
        		//Webページの形式や種類に関する情報取得(以上がなければステータスコードは200.)
                response.getContentType());
    }
}
