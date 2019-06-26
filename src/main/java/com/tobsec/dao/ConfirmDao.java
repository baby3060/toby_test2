package com.tobsec.dao;

import com.tobsec.model.Confirm;

import java.util.List;

/**
 * 붋만 사항의 경우 수정 없음
 */
public interface ConfirmDao {
    int addConfirm(Confirm confirm);
    int deleteConfirm(Confirm confirm);
    // 테스트용 전체 삭제
    void deleteAll();

    int getMaxSeq(String id, int confirm_date);
    int countAllUser(String id);
    
    int countUserDate(String id, int confirm_date);
    int countConfirm(String id, int confirm_date, int confirm_seq);
    void deleteAllUser(String id);

    Confirm getConfirm(String id, int confirm_date, int confirm_seq);

    // 미해결 리스트(유저별)
    List<Confirm> selectNoSolveByUser(String id);
    // 주어진 일자 사이에 미해결 리스트(유저별)
    List<Confirm> selectNoSolveByUserBetDt(String id, int date_from, int date_to);

    // 주어진 일자 사이에 미해결 리스트
    List<Confirm> selectNoSolveBetDt(int date_from, int date_to);

    // 해결은 되었는데 해당 유저가 아직 확인해주지 않은 것.
    List<Confirm> selectSolveNoCheckUser(String id);

    // 해당 유저가 확인해준거, 해결일자 Between
    List<Confirm> selectSolveCheckUserSDt(String id, int date_from, int date_to);

    // 해당 유저가 확인해준거, 모든 내역
    List<Confirm> selectSolveCheckUser(String id);

    // 운영자가 불만사항 해결
    void updateConfirmSolve(Confirm confirm);

    // 유저가 확인
    void updateUserOk(Confirm confirm);

    /**
     * 프로시저 확인용
     */
    void filedSolveContent();

    // 프로시저 작동 확인 용 테스트
    int countEmptySolveContent();
}