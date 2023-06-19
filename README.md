# omok
![Practice3_OmokApp_Team1_1](https://github.com/Team-doGet/omok/assets/106823684/14ee48ce-f8ae-485e-bbd7-0c7981ed5455)

## 아키텍처

- front: Thymeleaf
- back: Spring boot
  ![Practice3_OmokApp_Team1_2](https://github.com/Team-doGet/omok/assets/106823684/14fd58ef-2461-4d39-a923-c3b3356f8ed5)
  ![Practice3_OmokApp_Team1_3](https://github.com/Team-doGet/omok/assets/106823684/9858c361-3f54-4484-b4f9-81e00bfdfe34)

## 화면설계 및 유저 시나리오

![Practice3_OmokApp_Team1_7](https://github.com/Team-doGet/omok/assets/106823684/326fef9e-5aef-4fc0-9de1-5005eef100e1)
![Practice3_OmokApp_Team1_8](https://github.com/Team-doGet/omok/assets/106823684/327546f4-928f-4b1d-9194-04c2b00263f9)

## 기능

### 로그인/github

![Practice3_OmokApp_Team1_4](https://github.com/Team-doGet/omok/assets/106823684/c7334e87-9dc8-4aac-ae73-eaf5a5f0c594)

## 소켓통신

![Practice3_OmokApp_Team1_5](https://github.com/Team-doGet/omok/assets/106823684/32d9c412-f3dd-4d4e-97cd-a19107bac42d)
![Practice3_OmokApp_Team1_6](https://github.com/Team-doGet/omok/assets/106823684/b991b573-0d28-4d75-864d-987da564ae9a)

### 채팅

![Practice3_OmokApp_Team1_9](https://github.com/Team-doGet/omok/assets/106823684/8ebd3873-5818-421b-b730-3b60a86dd87f)
![Practice3_OmokApp_Team1_10](https://github.com/Team-doGet/omok/assets/106823684/f65ae8e7-f9e9-48e4-951d-808a2de1c342)
![Practice3_OmokApp_Team1_11](https://github.com/Team-doGet/omok/assets/106823684/8a250fc8-e452-4082-a638-4a5b269d82d9)

### 매칭

![Practice3_OmokApp_Team1_12](https://github.com/Team-doGet/omok/assets/106823684/ee97a38d-639c-4c9b-a0fd-7ff3ec48f0f7)
![Practice3_OmokApp_Team1_13](https://github.com/Team-doGet/omok/assets/106823684/c7685582-2a8e-474f-aaaa-3600ddcc3d73)
![Practice3_OmokApp_Team1_14](https://github.com/Team-doGet/omok/assets/106823684/dacb7b42-7800-497d-9a01-a9b6e8cd765a)

### 게임

![Practice3_OmokApp_Team1_15](https://github.com/Team-doGet/omok/assets/106823684/3346dbfc-4fb6-4eb7-97c8-9adbb7d2e541)
![Practice3_OmokApp_Team1_16](https://github.com/Team-doGet/omok/assets/106823684/7fad272c-42a6-4b61-8bc4-d1927c58f686)
![Practice3_OmokApp_Team1_17](https://github.com/Team-doGet/omok/assets/106823684/d8e97c1f-00c0-4fef-b067-ed1e5125b561)
![Practice3_OmokApp_Team1_18](https://github.com/Team-doGet/omok/assets/106823684/2be4e8f3-e159-4fd1-9091-b3a35f4a2fb1)

### DB

- H2 사용

## 컨벤션

### 커밋 메시지 컨벤션

- 타입: 커밋의 성격을 나타내는 타입으로 다음 중 하나를 선택합니다.
    - feat: 새로운 기능 추가
    - fix: 버그 수정
    - docs: 문서 수정
    - style: 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
    - refactor: 코드 리팩토링
    - test: 테스트 코드, 리팩토링 테스트 코드 추가
    - chore: 빌드, 패키지 매니저 수정 등
- 제목: 커밋의 간단한 제목입니다.
- 꼬리말: issues-ID나 커밋에 관련된 참고 정보 등을 포함합니다.

### 브랜치 전략

- main, development, back, front, hotfix 브랜치로 구성됩니다.

- main: 실제 배포되는 소스 코드가 저장되는 브랜치입니다.
- development: 개발 코드가 저장되는 브랜치입니다.
- feature: 새로운 기능을 개발하기 위한 브랜치입니다.
- back/front#형식으로 브랜치를 생성합니다. 개발할 기능마다 새로운 브랜치를 생성합니다.
  개발이 완료되면 development 브랜치에 병합합니다.
- hotfix: 긴급한 버그 수정을 위한 브랜치입니다.
  main 브랜치에서 발생한 버그를 수정하기 위해 생성합니다.
  수정이 완료되면 main 브랜치와 development 브랜치에 병합합니다.

## 참고자료

![Practice3_OmokApp_Team1_19](https://github.com/Team-doGet/omok/assets/106823684/742c61d9-746a-407c-8a48-3b6cda11b246)
