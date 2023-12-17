# bleuauction_be


## ERD
- [ERD경로](https://www.erdcloud.com/d/6MsT72YFLMMJ5ZQig)

## 변경한 Endpoint링크 기록페이지
- [링크](https://docs.google.com/spreadsheets/d/1Ljs4ez6YglGob7x7fvfL2vVOmiMuWcL2Ne-vYhN7Ezw/edit?usp=sharing)

## Service Architecture
아래와 같은 구조로 진행이 될예정
`@ComponentService`, `@ModuleService` 두개의 어노테이션 모두 Service와 동일한 역할을 하지만,
코드를 읽는 사람의 가독성을 위하여 분류함.

![ServiceArchitecture](https://blog.kakaocdn.net/dn/w6vFc/btscRr5Lz2q/iWIghTxYHYUHZ1tQKnZZI1/img.png)

### Annotation별 역할
- `@ComponentService` : ComponentService는 비즈니스를 처리해야 하는, 타 서비스의 의존을 받아야 하는 기능의 Service Layer
- `@ModuleService` : ModuleService는 비즈니스를 처리하지 않고, 단순히 CRUD를 위한 기능의 Service Layer