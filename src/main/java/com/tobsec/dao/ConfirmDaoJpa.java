package com.tobsec.dao;

import com.tobsec.common.Log;
import org.slf4j.Logger;

import com.tobsec.model.Confirm;
import com.tobsec.model.ConfirmKey;
import com.tobsec.common.JpaTransaction;

import java.util.List;
import javax.persistence.*;

import java.sql.Timestamp;

import org.springframework.stereotype.Repository;

@Repository("confirmDaoJpa")
public class ConfirmDaoJpa implements ConfirmDao {

    @Log
    protected Logger jpaTestLogger;

    @PersistenceContext
    private EntityManager em;

    @JpaTransaction
    public int addConfirm(Confirm confirm) {

        em.persist(confirm);
        em.flush();
        
        return 1;
    }

    @JpaTransaction
    public int deleteConfirm(Confirm confirm) {
        Confirm findConfirm = em.find(Confirm.class, new ConfirmKey(confirm.getApproval().getId(), confirm.getConfirm_date(), confirm.getConfirm_seq()));

        em.remove(findConfirm);

        em.flush();
        return 1;
    }

    @JpaTransaction
    public void deleteAll() {
        em.createQuery("Delete From Confirm").executeUpdate();

        em.flush();
    }

    public int getMaxSeq(String id, int confirm_date) {
        TypedQuery<Long> query = em.createQuery("Select Max(c.confirm_seq) " +
                                                " From CONFIRM c " +
                                                " Where c.id = :id " +
                                                " And c.confirm_date = :confirm_date", Long.class)
                                    .setParameter("id", id)
                                    .setParameter("confirm_date", confirm_date);

        return (int)(query.getSingleResult().longValue());
    }

    public int countAllUser(String id) {
        return (int)(em.createQuery("Select Count(c) From Confirm c Where c.approval.id = :id", Long.class).setParameter("id", id).getSingleResult().longValue());
    }
    
    public int countUserDate(String id, int confirm_date) {
        TypedQuery<Long> query = em.createQuery("Select Count(c) " +
                                        " From Confirm c  " +
                                        " Where c.id = :id " +
                                        " And c.confirm_date = :confirm_date", Long.class)
                        .setParameter("id", id)
                        .setParameter("confirm_date", confirm_date);
        
        return (int)(query.getSingleResult().longValue());
    }
    
    public int countConfirm(String id, int confirm_date, int confirm_seq) {
        TypedQuery<Long> query = em.createQuery("Select Count(c) " +
                                        " From Confirm c  " +
                                        " Where c.id = :id " +
                                        " And c.confirm_date = :confirm_date " +
                                        " And c.confirm_seq = :confirm_seq", Long.class)
                        .setParameter("id", id)
                        .setParameter("confirm_date", confirm_date)
                        .setParameter("confirm_seq", confirm_seq);
        
        return (int)(query.getSingleResult().longValue());                   
    }

    @JpaTransaction
    public void deleteAllUser(String id) {
        em.createQuery("Delete From Confirm c Where c.approval.id = :id").setParameter("id", id).executeUpdate();

        em.flush();
    }

    // 미해결 리스트(유저별)
    public List<Confirm> selectNoSolveByUser(String id) {
        return em.createNativeQuery(" Select *  " + 
                                    " From CONFIRM a " + 
                                    " Inner Join USER b On (b.id = a.id) " + 
                                    " Where a.id = :id " + 
                                    " And Ifnull(a.checkflagad, 'N') = 'N' " + 
                                    " Order By a.confirm_date ", Confirm.class)
                 .setParameter("id", id)
                .getResultList();
    }
    // 주어진 일자 사이에 미해결 리스트(유저별)
    public List<Confirm> selectNoSolveByUserBetDt(String id, int date_from, int date_to) {
        return em.createNativeQuery(" Select * " +
                                    " From CONFIRM a " +
                                    " Inner Join USER b On (b.id = a.id) " +
                                    " Where a.id = :id " +
                                    " And DATE_FORMAT(a.solve_timestamp, '%Y%m%d') Between :date_from And :date_to " +
                                    " And Ifnull(a.checkflagad, 'N') = 'Y'  " +
                                    " And Ifnull(a.checkflagus, 'N') = 'Y'  " +
                                    " Order By a.solve_timestamp ")
                 .setParameter("id", id)
                 .setParameter("date_from", date_from)
                 .setParameter("date_to", date_to)
                 .getResultList();
    }

    // 주어진 일자 사이에 미해결 리스트
    public List<Confirm> selectNoSolveBetDt(int date_from, int date_to) {
        return em.createNativeQuery("Select * " +
                                    " From Confirm a " +
                                    " Inner Join User b On (b.id = a.id) " +
                                    "Where a.confirm_date Between :date_from And :date_to " +
                                    "And Ifnull(a.checkflagad, 'N') = 'N' " +
                                    " Order By a.id, a.confirm_date, a.confirm_seq ", Confirm.class)
                  .setParameter("date_from", date_from)
                  .setParameter("date_to", date_to)
                 .getResultList();
    }

    // 해결은 되었는데 해당 유저가 아직 확인해주지 않은 것.
    public List<Confirm> selectSolveNoCheckUser(String id) {
        String option = "";
        if( id.equals("") ) {
            option = " Where ";
        } else {
            option = " Where a.id = :id And ";
        }
        
        Query query = em.createNativeQuery(" Select * " +
                                            " From CONFIRM a " +
                                            " Inner Join USER b On (b.id = a.id) " +
                                            option +
                                            " Ifnull(a.checkflagad, 'N') = 'Y' " +
                                            " And Ifnull(a.checkflagus, 'N') = 'N' " +
                                            " Order By a.confirm_date ", Confirm.class);

        if( option.length() > 10 ) {
            query = query.setParameter("id", id);
        }

        return query.getResultList();
    }

    // 해당 유저가 확인해준거, 해결일자 Between
    public List<Confirm> selectSolveCheckUserSDt(String id, int date_from, int date_to) {
        
        return em.createNativeQuery("Select * " +
                                    " From Confirm a " +
                                    " Inner Join User b On (b.id = a.id) " +
                                    " Where a.id = :id " +
                                    " And DATE_FORMAT(a.solve_timestamp, '%Y%m%d') Between :date_from And :date_to " +
                                    " And Ifnull(a.checkflagad, 'N') = 'Y'  " +
                                    " And Ifnull(a.checkflagus, 'N') = 'Y'  " +
                                    " Order By a.solve_timestamp ", Confirm.class)
                    .setParameter("id", id)
                    .setParameter("date_from", date_from)
                    .setParameter("date_to", date_to)
                    .getResultList();
    }

    // 해당 유저가 확인해준거, 모든 내역
    public List<Confirm> selectSolveCheckUser(String id) {

        return em.createNativeQuery("Select * " +
                                    " From Confirm a " +
                                    " Inner Join User b On (b.id = a.id) " +
                                    " Where a.id = :id " +
                                    " And Ifnull(a.checkflagad, 'N') = 'Y'  " +
                                    " And Ifnull(a.checkflagus, 'N') = 'Y'  " +
                                    " Order By a.solve_timestamp ", Confirm.class)
                    .setParameter("id", id)
                    .getResultList();

    }

    @JpaTransaction
    public void updateConfirmSolve(Confirm confirm) {
        
        Confirm findConfirm = em.find(Confirm.class, new ConfirmKey(confirm.getApproval().getId(), confirm.getConfirm_date(), confirm.getConfirm_seq()));
        findConfirm.setSolve_content(confirm.getSolve_content());
        findConfirm.setCheckflagad("Y");
        
        em.flush();
    }

    @JpaTransaction
    public void updateUserOk(Confirm confirm) {
        Confirm findConfirm = em.find(Confirm.class, new ConfirmKey(confirm.getApproval().getId(), confirm.getConfirm_date(), confirm.getConfirm_seq()));

        findConfirm.setSolve_timestamp(new Timestamp(System.currentTimeMillis()));
        findConfirm.setCheckflagus("Y");
        
        em.flush();
    }

    @JpaTransaction
    public void filedSolveContent() {
        em.createNativeQuery("call sp_auto_solve_content_fill()").executeUpdate();

        em.flush();
    }

    // 프로시저 작동 확인 용 테스트
    public int countEmptySolveContent() {
        TypedQuery<Long> query = em.createQuery("Select Count(c) From Confirm c Where c.solve_content Is Null", Long.class);

        return (int)(query.getSingleResult().longValue());
    }

    public Confirm getConfirm(String id, int confirm_date, int confirm_seq) {
        return em.find(Confirm.class, new ConfirmKey(id, confirm_date, confirm_seq));
    }
}