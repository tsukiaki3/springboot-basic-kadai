package com.example.springtutorial.rps;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//★ 複数のクラスやメソッドを用いる結合テストでは @SpringBootTestアノテーションをつける
public class RockPaperScissorsTest {
    // テスト対象クラスのオブジェクト(テストクラスで保持したいデータやオブジェクトがあれば、フィールドとして宣言)
    private RockPaperScissors game;

    //@BeforeEachアノテーションを用いて、テスト対象オブジェクトの初期化処理
    @BeforeEach
    void init() {
        // 初期化処理
        game = new RockPaperScissors();
    }

    //テストを実行するためには @Testアノテーション。@TestをつけるとJUnitのテスト実行時に呼び出し
    @Test
    void testRockPaperScissors() {
        // 各テストケースを実施
    	
    	//データの一致判定に使えるassertEquals()メソッド
    	//第1引数には各テストケースの期待値、第2引数には テスト対象メソッドの呼び出し処理を記述
        assertEquals("あいこ", game.judgeResult("グー", "グー"));
        assertEquals("勝ち", game.judgeResult("グー", "チョキ"));
        assertEquals("負け", game.judgeResult("グー", "パー"));
        assertEquals("無効試合", game.judgeResult("グー", "dummy"));
        assertEquals("負け", game.judgeResult("チョキ", "グー"));
        assertEquals("あいこ", game.judgeResult("チョキ", "チョキ"));
        assertEquals("勝ち", game.judgeResult("チョキ", "パー"));
        assertEquals("無効試合", game.judgeResult("チョキ", "dummy"));
        assertEquals("勝ち", game.judgeResult("パー", "グー"));
        assertEquals("負け", game.judgeResult("パー", "チョキ"));
        assertEquals("あいこ", game.judgeResult("パー", "パー"));
        assertEquals("無効試合", game.judgeResult("パー", "dummy"));
        assertEquals("無効試合", game.judgeResult("dummy", "dummy"));
    }
}
