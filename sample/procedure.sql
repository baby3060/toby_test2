create or replace procedure sp_auto_solve_content_fill() 
begin
	
	DECLARE done INT DEFAULT FALSE;
	DECLARE ls_cnt int(3) default 0;

	DECLARE cur_id varchar(10);
	DECLARE cur_confirm_date int(8);
	DECLARE cur_confirm_seq int(3);

	DECLARE vRowCount INT DEFAULT 0 ;

	DECLARE confirm_cur CURSOR FOR 
	SELECT id, confirm_date, confirm_seq
	from confirm
	where ifnull(checkflagad, 'N') = 'Y'
	and   ifnull(checkflagus, 'N') = 'Y'
	and   solve_content is null;	

    -- no data fetch 해결 위한
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	
	select count(*) into ls_cnt
	from confirm
	where ifnull(checkflagad, 'N') = 'Y'
	and   ifnull(checkflagus, 'N') = 'Y'
	and   solve_content is null;	
	
	IF ls_cnt > 0 then
		
		OPEN confirm_cur;
		
		confirm_loop:LOOP

            FETCH confirm_cur INTO cur_id, cur_confirm_date, cur_confirm_seq;
			IF done THEN
				LEAVE confirm_loop;
			END IF; 

			SET vRowCount = vRowCount +1 ;
			
			update confirm
                set solve_content = '자동 입력'
                where id = cur_id
                and   confirm_date = cur_confirm_date
                and   confirm_seq = cur_confirm_seq;
			
		END loop confirm_loop;

		SELECT vRowCount ;

		CLOSE confirm_cur;

        commit;
	END IF;
end;