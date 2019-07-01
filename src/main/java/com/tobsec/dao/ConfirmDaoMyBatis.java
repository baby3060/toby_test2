package com.tobsec.dao;

import java.util.*;
import com.tobsec.model.*;

// import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.session.SqlSessionFactory;

@Repository("confirmDaoBatis")
public class ConfirmDaoMyBatis extends SqlSessionDaoSupport implements ConfirmDao {
    /*
    @Autowired
    private SqlSession sqlSession;
    */

    @Autowired 
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) { 
        super.setSqlSessionFactory(sqlSessionFactory); 
    }

    public int addConfirm(Confirm confirm) {
        return this.getSqlSession().insert("mapper.mybatis.ConfirmMapper.addConfirm", confirm);
    }

    public int deleteConfirm(Confirm confirm) {
        return this.getSqlSession().delete("mapper.mybatis.ConfirmMapper.deleteConfirm", confirm);
    }
    // 테스트용 전체 삭제
    public void deleteAll() {
        this.getSqlSession().delete("mapper.mybatis.ConfirmMapper.deleteAllConfirm");
    }

    public int getMaxSeq(String id, int confirm_date) {
        HashMap<String, Object> param = new HashMap<String, Object>();

        param.put("id", id);
        param.put("confirm_date", confirm_date);

        return this.getSqlSession().selectOne("mapper.mybatis.ConfirmMapper.getMaxSeq", param);
    }

    public int countAllUser(String id) {
        HashMap<String, Object> param = new HashMap<String, Object>();

        param.put("id", id);

        return this.getSqlSession().selectOne("mapper.mybatis.ConfirmMapper.countAllUser", param);
    }
    
    public int countUserDate(String id, int confirm_date) {
        HashMap<String, Object> param = new HashMap<String, Object>();

        param.put("id", id);
        param.put("confirm_date", confirm_date);

        return this.getSqlSession().selectOne("mapper.mybatis.ConfirmMapper.countUserDate", param);
    }

    public int countConfirm(String id, int confirm_date, int confirm_seq) {
        HashMap<String, Object> param = new HashMap<String, Object>();

        param.put("id", id);
        param.put("confirm_date", confirm_date);
        param.put("confirm_seq", confirm_seq);

        return this.getSqlSession().selectOne("mapper.mybatis.ConfirmMapper.countConfirm", param);
    }

    public void deleteAllUser(String id) {
        HashMap<String, Object> param = new HashMap<String, Object>();

        param.put("id", id);

        this.getSqlSession().delete("mapper.mybatis.ConfirmMapper.deleteAllUser", param);
    }

    // 미해결 리스트(유저별)
    public List<Confirm> selectNoSolveByUser(String id) {
        HashMap<String, Object> param = new HashMap<String, Object>();

        param.put("id", id);

        return this.getSqlSession().selectList("mapper.mybatis.ConfirmMapper.selectNoSolveByUser", param);
    }

    // 주어진 일자 사이에 미해결 리스트(유저별)
    public List<Confirm> selectNoSolveByUserBetDt(String id, int date_from, int date_to) {
        HashMap<String, Object> param = new HashMap<String, Object>();

        param.put("id", id);
        param.put("date_from", date_from);
        param.put("date_to", date_to);

        return this.getSqlSession().selectList("mapper.mybatis.ConfirmMapper.selectNoSolveByUserBetDt", param);
    }

    // 주어진 일자 사이에 미해결 리스트
    public List<Confirm> selectNoSolveBetDt(int date_from, int date_to) {
        HashMap<String, Object> param = new HashMap<String, Object>();

        param.put("date_from", date_from);
        param.put("date_to", date_to);

        return this.getSqlSession().selectList("mapper.mybatis.ConfirmMapper.selectNoSolveBetDt", param);
    }

    // 해결은 되었는데 해당 유저가 아직 확인해주지 않은 것.
    public List<Confirm> selectSolveNoCheckUser(String id) {

        HashMap<String, Object> param = new HashMap<String, Object>();

        param.put("id", id);

        return this.getSqlSession().selectList("mapper.mybatis.ConfirmMapper.selectSolveNoCheckUser", param);
    }

    // 해당 유저가 확인해준거, 해결일자 Between
    public List<Confirm> selectSolveCheckUserSDt(String id, int date_from, int date_to) {
        HashMap<String, Object> param = new HashMap<String, Object>();

        param.put("id", id);
        param.put("date_from", date_from);
        param.put("date_to", date_to);

        return this.getSqlSession().selectList("mapper.mybatis.ConfirmMapper.selectSolveCheckUserSDt", param);
    }

    // 해당 유저가 확인해준거, 모든 내역
    public List<Confirm> selectSolveCheckUser(String id) {
        HashMap<String, Object> param = new HashMap<String, Object>();

        param.put("id", id);

        return this.getSqlSession().selectList("mapper.mybatis.ConfirmMapper.selectSolveCheckUser", param);
    }

    // 운영자가 불만사항 해결
    public void updateConfirmSolve(Confirm confirm) {
        this.getSqlSession().update("mapper.mybatis.ConfirmMapper.updateConfirmSolve", confirm);
    }

    // 유저가 확인
    public void updateUserOk(Confirm confirm) {
        this.getSqlSession().update("mapper.mybatis.ConfirmMapper.updateUserOk", confirm);
    }

    /**
     * 프로시저 확인용
     */
    public void filedSolveContent() {
        this.getSqlSession().update("mapper.mybatis.ConfirmMapper.filedSolveContent");
    }

    // 프로시저 작동 확인 용 테스트
    public int countEmptySolveContent() {
        return this.getSqlSession().selectOne("mapper.mybatis.ConfirmMapper.countEmptySolveContent");
    }

    public Confirm getConfirm(String id, int confirm_date, int confirm_seq) {
        return null;
    }

    
    public List<Confirm> selectAllList() {
        return null;
    }
}