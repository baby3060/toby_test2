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
- [ ] Connection 연결 시 필요한 정보에 작성할 XML을 XJC 사용하여 형식 만들기. 
- [ ] ConnectionBean의 설정 정보 Jaxb, Castor 사용해보기
- [ ] ConnectionBean의 설정 정보를 XML 뿐만 아니라, json에서 읽어보기
