# toby_test2
토비 스프링 두 번째(약간 개정)

1. ConnectionBean을 이전에는 xml의 factoryMethod(XmlParsing)를 이용하여, 생성하였으나 이번에는 FactoryBean을 구현한 클래스에서 생성.
> 이전에 작성한 버전
<pre>
    <code>
        &lt;bean id="xmlParsing" class="com.tobexam.common.XMLParsingConfig"&gt;
            &lt;property name="fileName" value="mysql_conn.xml" /&gt;
        &lt;/bean&gt;

        &lt;bean id="connBean" class="com.tobexam.common.ConnectionBean"
            factory-bean="xmlParsing" factory-method="setConfig" /&gt;
    </code>
</pre>
>> Todo List
- [X] Connection 연결 시 필요한 정보를 XML에 저장하여, Bean으로 작성하기.
- [X] Connection 연결 시 필요한 정보에 작성할 XML을 XJC 사용하여 형식 만들기. 
>>> 참조 URL : https://thoughts-on-java.org/generate-your-jaxb-classes-in-second/
- [X] ConnectionBean의 설정 정보 Jaxb, Castor 사용해보기
- [X] ConnectionBean의 설정 정보를 XML 뿐만 아니라, json에서 읽어보기

2. UserDao, ConfirmDao 생성(DataSource 넘겨받는)
>> Todo List
- [X] UserDao 인터페이스로 생성
- [X] UserDao JDBC 용 구현 클래스 생성
>>> 동적 SQL 생성 시 파라미터를 DAO 메소드로 가져오는 것도 생각하였으나, 그것은 Dao "자체의 역할"과는 전혀 별개의 것 같아서 서비스 단에서 검색문을 만드는 것으로 결론
- [X] Jdbc 동적 조회 쿼리 생성(whereOption)
- [X] ConfirmDao 인터페이스로 생성(Procedure 호출 구현) 및 JDBC 용 구현 클래스 생성
- [ ] 조회용 RowMapper를 제공하는 클래스 구현(User, Confirm 둘 다 사용) 및 사용.
- [ ] MyBatis로 구현(UserDao, ConfirmDao 둘 다)

3. 해당 Dao 사용하는 Service 객체 생성
>> Todo List
- [ ] UserService 인터페이스로 생성
- [ ] UserService 구현하는 클래스 생성
- [ ] UserService에서 동적 조회 쿼리 이용 구문 테스트
- [ ] UserService에서 유효성 검사(추가 시 카운트가 0인지? 유효한 데이터인지 등)
- [ ] 트랜잭션 어드바이저 등록(존재할 경우)
- [ ] Confirm도 동일
- [ ] Sql 가져올 방법 모색(Json : GENSON, XML : JAXB, CASTOR, 내장 DB : H2)