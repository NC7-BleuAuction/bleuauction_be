# bleuauction_be


## ERD
- [ERD경로](https://www.erdcloud.com/d/6MsT72YFLMMJ5ZQig)
### 변경 필요 사항
- Order
  - 주문자 정보가 없슴. 주문자 정보 매핑 추가가 필요함
  - 가게 정보도 없슴. 가게 정보 매핑 추가가 필요함
  - 여기서 orderPrice는 TotalPrice로 변경해야하는게 좀더 맞는 표현이 아닌가 싶음.
- OrderStatus
  - 주문완료에 대한 상태값이 필요하다고 느껴짐, 주문검색시 따로 분류가 필요함.

- OrderMenu
  - Order에 주문자 정보를 넣고, OrderMenu는 빼는게 맞지 않는지 생각이 듦.

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