package com.tobsec.service;

import java.util.List;
import java.util.Objects;

import com.tobsec.common.*;

import com.tobsec.model.User;
import com.tobsec.model.Level;
import com.tobsec.dao.UserDao;

import com.tobsec.service.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Service("testService")
    public static class TestUserServiceImpl extends UserServiceImpl {
        private String id = "5";

        protected void upgradeLevel(User user) {
            
            if(user.getId().equals(this.id)) {
                throw new TestUserServiceException();
            } else {
                super.upgradeLevel(user);
            }
        }

        @Override
        public void addUser(User user) throws EmptyResultException{
            int result = 0;

            if(user == null) {
                result = 0;
            } else {
                if( super.countUser(user.getId()) == 0 ) {
                    // Java 7 이상에서 지원 Null String을 ""로
                    user.setRecid(Objects.toString(user.getRecid(), "").trim());
                    result = userDao.addUser(user);
                }
            }

            if( result < 1 ) {
                throw new EmptyResultException("저장 중 에러가 발생하였습니다. 데이터가 저장되지 않았습니다.");
            }
        }
    }

    public static class TestUserServiceException extends RuntimeException { }

    @Autowired
    UserDao userDao;

    @Autowired
    private LevelUpStrategy levelUpStrategy;

    @Override
    public void addUser(User user) throws EmptyResultException{
        int result = 0;

        logger.info("UserService.addUser Called");

        if(user == null) {
            result = 0;
        } else {
            if( this.countUser(user.getId()) == 0 ) {
                
                // 등급은 BRONZE로
                user.setLevel(Level.BRONZE);
                // 신규 유저 등록일 경우 무조건 둘 다 0
                user.setLogin(0);
                user.setRecommend(0);

                // Java 7 이상에서 지원 Null String을 ""로
                user.setRecid(Objects.toString(user.getRecid(), "").trim());

                if( ((!user.getRecid().equals("")) && (this.countUser(user.getRecid()) == 1)) ) {
                    // 추천 대상
                    User target = this.getUser(user.getRecid());

                    userDao.plusRecommend(target, 1);
                } else {
                    user.setRecid("");
                }

                logger.info("Arguments User : " + user);

                result = userDao.addUser(user);
            }
        }

        logger.info("UserService.addUser End");

        if( result < 1 ) {
            throw new EmptyResultException("저장 중 에러가 발생하였습니다. 데이터가 저장되지 않았습니다.");
        }
    }

    @Override
    public void updateUser(User user) throws EmptyResultException{
        int result = 0;

        logger.info("UserService.updateUser Called");

        if(user == null) {
            result = 0;
        } else {
            if( this.countUser(user.getId()) == 1 ) {
                result = userDao.updateUser(user);
            }
        }

        logger.info("UserService.updateUser End");

        if( result < 1 ) {
            throw new EmptyResultException("저장 중 에러가 발생하였습니다. 데이터가 저장되지 않았습니다.");
        }
    }

    @Override
    public void deleteUser(User user) throws EmptyResultException{
        int result = 0;

        logger.info("UserService.deleteUser Called");

        if(user == null) {
            result = 0;
        } else {
            if( this.countUser(user.getId()) == 1 ) {
                result = userDao.deleteUser(user.getId());
            }
        }

        logger.info("UserService.deleteUser End");

        if( result < 1 ) {
            throw new EmptyResultException("저장 중 에러가 발생하였습니다. 데이터가 저장되지 않았습니다.");
        }
    }

    @Override
    @Transactional(readOnly=true)
    public User getUser(String id) {
        return userDao.getUser(id);
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    @Override
    @Transactional(readOnly=true)
    public int countUser(String id) {
        return userDao.countUser(id);
    }

    @Override
    @Transactional(readOnly=true)
    public int countAll() {
        return userDao.countUserAll();
    }

    @Override
    public void upgradeLevels() throws LevelUpFailException {
        List<User> users = this.selectUserAll();
        boolean changed = false;
        for( User user : users ) {
            try {
                if( user.isLvlUpTarget(levelUpStrategy) ) {
                    upgradeLevel(user);
                } 
            } catch(IllegalStateException ise) {
                throw new LevelUpFailException(ise.getMessage(), ise);
            }
        }
    }

    // Test를 위하여 protected로 지정
    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        this.updateUser(user);
    }

    /**
     * 실제로 사용되어야 하는 메소드
     */
    public void plusLogin(User user) { 
        userDao.plusLogin(user, 1);
    }

    /**
     * target : 추천수 증가시킬 User
     * recoUser : target을 추천한 User
     */
    public void plusRecommend(User target, User recoUser) {
        userDao.plusRecommend(target, 1);
        userDao.checkedRecommend(recoUser);
    }

    @Override
    @Transactional(readOnly=true)
    public int countUserLevel(Level level, String gubun, Level toLevel) {
        String option = "";
        logger.info("UserService.countUserLevel Called");
        int levelValue = level.getValue();

        if( gubun.equals("EQ") ) {
            option = " And level = " + levelValue + " ";
        } else if( gubun.equals("OV")) {
            option = " And level > " + levelValue + " ";
        } else if(gubun.equals("UN")) {
            option = " And level < " + levelValue + " ";
        } else {
            option = " And level Between " + levelValue + " And " + toLevel.getValue() + " ";
        }

        logger.info("UserService.countUserLevel End option = " + option);

        return userDao.countUserCondition(option);
    }
    
    @Override
    @Transactional(readOnly=true)
    public List<User> selectUserAll() {
        return userDao.selectUserAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addUserNew(User user) {
        logger.info("UserService.addUserNew Called");
        if(user == null) {
            
        } else {
            if( this.countUser(user.getId()) == 0 ) {
                // Java 7 이상에서 지원 Null String을 ""로
                user.setRecid(Objects.toString(user.getRecid(), "").trim());
                userDao.addUser(user);
            }
        }
    }

    @Override
    @SecureAccess("level.getValue() >= T(com.tobsec.model.Level).GOLD.getValue()")
    public void goldOverAcceable(User user) {
        logger.info(user.getId() + "회원의 레벨은 GOLD 레벨이상입니다. 해당 메소드 수행 가능");
    }
}