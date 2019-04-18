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
- [X] ConnectionBean으로 저장하는 것이 아닌 스프링의 Properties 활용 클래스 사용하여 Connection 생성해보기
- [ ] DataSource 생성 시 Profile 사용해보기(일단 파일을 쪼개서, 그리고 나서 하나의 파일에 : xml, Java 파일에도 저장[Configuration으로 만들어서])

2. UserDao, ConfirmDao 생성(DataSource 넘겨받는)
>> Todo List
- [X] UserDao 인터페이스로 생성
- [X] UserDao JDBC 용 구현 클래스 생성
>>> 동적 SQL 생성 시 파라미터를 DAO 메소드로 가져오는 것도 생각하였으나, 그것은 Dao "자체의 역할"과는 전혀 별개의 것 같아서 서비스 단에서 검색문을 만드는 것으로 결론
- [X] Jdbc 동적 조회 쿼리 생성(whereOption)
- [X] ConfirmDao 인터페이스로 생성(Procedure 호출 구현) 및 JDBC 용 구현 클래스 생성
>>> MariaDB에서 Procedure 생성 시 BEGIN과 Declare 문 사이에 다른게 하나도 없어야 한다.
>>> DONE을 이용하여 FETCH 관련 NO DATA ~ 해결
- [X] 조회용 RowMapper를 제공하는 클래스 구현(User, Confirm 둘 다 사용) 및 사용.
>>> 특별한 처리를 필요로 하지 않는다면(User의 Level과 같은), BeanPropertyRowMapper를 사용하는 것이 편할 것 같다.
- [X] MyBatis로 구현(UserDao, ConfirmDao 둘 다) : UserDao 완료
>>> Option(Service에서 받아온 조건문 그대로 사용)을 그대로 사용하려면 Map으로 매핑한 후 $를 사용하여 적용할 수 있었다.
>>> Enum형을 저장하거나, 변환할 때 숫자형일 경우 org.apache.ibatis.type.EnumOrdinalTypeHandler를 사용함(String은 또 다른 핸들러). 
>>> 이 핸들러는 ENUM에 저장된 순서가 중요함(User의 Level과 같은 형태는 맞지 않음).
>>>> 따라서, EnumOrdinalTypeHandler가 상속받는 BaseTypeHandler<E>용으로 하나 만들어야 함(LevelOnlyHandler).


3. 해당 Dao 사용하는 Service 객체 생성
>> Todo List
- [X] UserService 인터페이스로 생성
- [X] UserService 구현하는 클래스 생성
- [X] 회원 등급 상승 적용(이벤트 대비 전략)
>>> 모든 List를 다 가져오고 나서, 대상이라면 상승
- [X] UserService에서 동적 조회 쿼리 이용 구문 테스트
- [X] UserService에서 유효성 검사(추가 시 카운트가 0인지? 유효한 데이터인지 등)
- [X] 트랜잭션 적용(애노테이션)
>>> @Transactionl 애노테이션의 경우 Dao에 적용할 수도 있고, Service에도 적용할 수 있는데 어지간하면 Service에 적용하는 편이 더 낫다.
>>>> User의 경우 @Transactional 사용, Confirm은 aop 네임스페이스의 전용태그 사용
>>>> @Transactional은 메소드별로 좀 더 세부적인 설정 가능, 다만 자바 코드에 작성해야 함(소스 라인 증가).
>>>> aop 네임스페이스의 전용태그는 반대 : 소스코드는 증가하지 않고 트랜잭션 가능하지만, 메소드별로 세부적인 사항은 적용하기 힘듦
>>>> Before : 비즈니스 메소드 실행 전 실행(다이내믹 프록시에서 타깃 메소드 실행 전에 : ret = invocation.proceed 호출 전)
>>>> After : 비즈니스 메소드 실행 후 실행(다이내믹 프록시에서 타깃 메소드 실행 전에 : ret = invocation.proceed 호출 후)
>>>>> After Returning : 비즈니스 메소드가 성공적으로 수행 후
>>>>> After Throwing : 비즈니스 메소드가 수행 중 예외 발생후 동작
>>>>> After : 비즈니스 메소드의 수행 결과에 상관없이 무조건 동작
>>>> Around : 메소드 실행 전 후에 처리할 로직
- [X] Sql 가져올 방법 모색(Json : GENSON, 내장 DB : H2) : JSON 완료, 내장 DB 완료
- [X] Logging 프레임워크 사용(LogBack)
>>> LogBack은 slf4j에 부가기능을 추가해주는 용도라고 생각하면 됨(실제 로깅은 slf4j것을 사용)
- [X] Logger 애노테이션 생성(후처리기 사용)
>>> ReflectionUtils 사용(java Reflect 사용하기 쉽게 해주는 스프링의 Util)
- [X] 경과 시간을 구할 Aspect 적용(ProceddingJoinPoint 사용) : 무조건 들어가야함.
- [X] BOARD 테이블(키 값은 자동 생성) 생성 
- [ ] 트랜잭션 어드바이스 적용 및 REQUIRED 속성의 트랜잭션 메소드에서 자신의 REQUIRES_NEW 속성 메소드 호출해보기(프록시 트랜잭션의 한계 체험 및 보완해보기)
>>>> ~~(@Aspect 애노테이션 사용)~~ @Aspect 애노테이션 사용해서 트랜잭션을 적용하는 것은 무리 : 이유는 트랜잭션 속성때문
- [ ] BoardDao의 InsertBoard에는 SimpleJdbcInsert 사용(Dao Support) : 생성된 Key 값을 반환
- [ ] BOARDDao의 UpdateBoard에서는 PreparedStatementCreator 사용
- [ ] BOARDDao MyBatis로도 생성
- [ ] Service Logging에 Before, After, AfterThrowing, AfterReturning 적용해보기

4. 코드 품질 개선
- [ ] Findbugs 사용
- [ ] PMD 사용

5. JavaDoc 생성