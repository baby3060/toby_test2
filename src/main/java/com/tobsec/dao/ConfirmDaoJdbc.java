package com.tobsec.dao;

import java.util.*;
import com.tobsec.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;


@Repository("confirmDao")
public class ConfirmDaoJdbc implements ConfirmDao {

    private final RowMapper<Confirm> confirmMapper = new RowMapper<Confirm>() {
        public Confirm mapRow(ResultSet rs, int rowNum) throws SQLException {
            Confirm confirm = new Confirm();

            confirm.setId(rs.getString("id"));
            confirm.setConfirm_date(rs.getInt("confirm_date"));
            confirm.setConfirm_seq(rs.getInt("confirm_seq"));
            confirm.setConfirm_time(rs.getString("confirm_time"));
            confirm.setContent(rs.getString("content"));
            confirm.setSolve_content(rs.getString("solve_content"));
            confirm.setCheckflagad(rs.getString("checkflagad"));
            confirm.setCheckflagus(rs.getString("checkflagus"));
            confirm.setSolve_timestamp(rs.getTimestamp("solve_timestamp"));

            return confirm;
        }
    };

    private SqlParameterSource makeParam(Confirm confirm) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue("id", confirm.getId());
        paramSource.addValue("confirm_date", confirm.getConfirm_date());
        paramSource.addValue("confirm_seq", confirm.getConfirm_seq());
        paramSource.addValue("confirm_time", confirm.getConfirm_time());
        paramSource.addValue("content", confirm.getContent());
        paramSource.addValue("solve_content", confirm.getSolve_content());
        paramSource.addValue("checkflagad", confirm.getCheckflagad());
        paramSource.addValue("checkflagus", confirm.getCheckflagus());
        paramSource.addValue("solve_timestamp", confirm.getSolve_timestamp());

        return paramSource;
    }

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    // 해당 ID로 등록된 모든 내역(정식으로는 이 내역이 있으면 삭제 안 되게 해야 함)
    public int countAllUser(String id) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("id", id);

        return this.jdbcTemplate.queryForObject("Select Count(*) As cnt From CONFIRM Where id = :id", param, Integer.class );
    }

    // Max Seqno 구할 때에만 사용할 거임
    public int countUserDate(String id, int confirm_date) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("id", id);
        param.addValue("confirm_date", confirm_date);

        return this.jdbcTemplate.queryForObject("Select Count(*) As cnt From CONFIRM Where id = :id And confirm_date = :confirm_date", param, Integer.class );
    }

    // 해당 ID로 등록된 모든 CONFIRM 삭제(회원탈퇴 시 쓰일 수 있음)
    public void deleteAllUser(String id) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("id", id);

        this.jdbcTemplate.update("Delete From CONFIRM Where id = :id", param);
    }

    // Add 시에만 사용(서비스 단에서 countUserDate가 0일 경우에만 호출)
    public int getMaxSeq(String id, int confirm_date) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("id", id);
        param.addValue("confirm_date", confirm_date);

        return this.jdbcTemplate.queryForObject("Select Max(confirm_seq) As confirm_seq From CONFIRM Where id = :id And confirm_date = :confirm_date", param, Integer.class);
    }

    // 원래는 무조건 오늘 일자를 넣는 것이 정상이지만 => (Select DATE_FORMAT(NOW(),'%Y%m%d'))
    // Seqno 가져오는 것과 맞추는 것이 옳아 보임
    public int addConfirm(Confirm confirm) {
        return this.jdbcTemplate.update("Insert Into CONFIRM(id, confirm_date, confirm_seq, confirm_time, content) Values (:id, :confirm_date, :confirm_seq, (Select DATE_FORMAT(NOW(),'%H%i%s')), :content) ", makeParam(confirm));
    }

    // 삭제 시 해당 일자의 가장 큰 순번일 경우에만 삭제 가능하게
    public int deleteConfirm(Confirm confirm) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("id", confirm.getId());
        param.addValue("confirm_date", confirm.getConfirm_date());
        param.addValue("confirm_seq", confirm.getConfirm_seq());

        return this.jdbcTemplate.update("Delete From CONFIRM Where id = :id And confirm_date = :confirm_date And confirm_seq = :confirm_seq", param );
    }
    
    // 미해결 리스트(유저별) : checkflagad = 'N'
    public List<Confirm> selectNoSolveByUser(String id) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("id", id);

        return this.jdbcTemplate.query("Select * From CONFIRM Where id = :id And Ifnull(checkflagad, 'N') = 'N' ", param, this.confirmMapper);
    }
    // 주어진 일자 사이에 미해결 리스트(유저별)
    public List<Confirm> selectNoSolveByUserBetDt(String id, int date_from, int date_to) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("id", id);
        param.addValue("date_from", date_from);
        param.addValue("date_to", date_to);

        return this.jdbcTemplate.query("Select * From CONFIRM Where id = :id And confirm_date Between :date_from And :date_to And Ifnull(checkflagad, 'N') = 'N' ", param, this.confirmMapper);
    }

    // 주어진 일자 사이에 미해결 리스트
    public List<Confirm> selectNoSolveBetDt(int date_from, int date_to) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("date_from", date_from);
        param.addValue("date_to", date_to);

        return this.jdbcTemplate.query("Select * From CONFIRM Where confirm_date Between :date_from And :date_to And Ifnull(checkflagad, 'N') = 'N' ", param, this.confirmMapper);
    }

    // 해결은 되었는데 해당 유저가 아직 확인해주지 않은 것.
    // checkflagad는 Y인데, checkflagus가 N인거
    public List<Confirm> selectSolveNoCheckUser(String id) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("id", id);

        return this.jdbcTemplate.query("Select * From CONFIRM Where id = :id And Ifnull(checkflagad, 'N') = 'Y' And Ifnull(checkflagus, 'N') = 'N' ", param, this.confirmMapper);
    }

    // 해당 유저가 확인해준거, 해결일자 Between
    public List<Confirm> selectSolveCheckUserSDt(String id, int date_from, int date_to) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("id", id);
        param.addValue("date_from", date_from);
        param.addValue("date_to", date_to);

        return this.jdbcTemplate.query("Select * From CONFIRM Where id = :id And DATE_FORMAT(solve_timestamp, '%Y%m%d') Between :date_from And :date_to And Ifnull(checkflagad, 'N') = 'Y' And Ifnull(checkflagus, 'N') = 'Y' Order By solve_timestamp ", param, this.confirmMapper);
    }

    // 해당 유저가 확인해준거, 모든 내역
    public List<Confirm> selectSolveCheckUser(String id) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("id", id);

        return this.jdbcTemplate.query("Select * From CONFIRM Where id = :id And Ifnull(checkflagad, 'N') = 'Y' And Ifnull(checkflagus, 'N') = 'Y' Order By solve_timestamp", param, this.confirmMapper);
    }

    // 미해결 내역을 해결로
    // checkflagad를 Y로
    // selectNoSolveByUser, selectNoSolveBetDt, selectNoSolveByUserBetDt
    public void updateConfirmSolve(Confirm confirm) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("id", confirm.getId());
        param.addValue("confirm_date", confirm.getConfirm_date());
        param.addValue("confirm_seq", confirm.getConfirm_seq());

        param.addValue("solve_content", confirm.getSolve_content());

        this.jdbcTemplate.update("Update CONFIRM Set checkflagad = 'Y', solve_content = :solve_content Where id = :id And confirm_date = :confirm_date And confirm_seq = :confirm_seq", param);   
    }

    // 유저 확인 시 solve_timestamp은 현재 시간, checkflagus는 Y
    // selectSolveNoCheckUser를 통해서 조회해온 것만 가능
    public void updateUserOk(Confirm confirm) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("id", confirm.getId());
        param.addValue("confirm_date", confirm.getConfirm_date());
        param.addValue("confirm_seq", confirm.getConfirm_seq());

        this.jdbcTemplate.update("Update CONFIRM Set checkflagus = 'Y', solve_timestamp = CURRENT_TIMESTAMP() Where id = :id And confirm_date = :confirm_date And confirm_seq = :confirm_seq", param);   
    }

    // checkflagad가 Y인데 내용이 비어있을 경우 공통 해결
    // 프로시저
    public void filedSolveContent() {
        MapSqlParameterSource param = new MapSqlParameterSource();

        jdbcTemplate.update("call sp_auto_solve_content_fill()", param);
    }

    /**
     * 프로시저 관련 임시
     */
    public int countEmptySolveContent() {
        MapSqlParameterSource param = new MapSqlParameterSource();

        return this.jdbcTemplate.queryForObject("Select Count(*) As empty_con From CONFIRM Where solve_content Is Null", param, Integer.class);
    }
}