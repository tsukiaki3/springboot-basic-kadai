package com.example.springtutorial.rps;

public class RockPaperScissors {
    // じゃんけんの勝敗を判定するメソッド
    public String judgeResult(final String myHand, final String yourHand) {

        // 引数が不正だったら無効試合
        switch (myHand) {
            case "グー", "チョキ", "パー" -> {}
            default -> {return "無効試合";}
        }
        switch (yourHand) {
            case "グー", "チョキ", "パー" -> {}
            default -> {return "無効試合";}
        }

        // 勝敗の判定
        if (myHand.equals(yourHand)) {
            return "あいこ";
        } else if ((myHand.equals("グー") && yourHand.equals("チョキ")) ||
                (myHand.equals("チョキ") && yourHand.equals("パー")) ||
                (myHand.equals("パー") && yourHand.equals("グー"))) {
            return "勝ち";
        } else {
            return "負け";
        }
    }
}
