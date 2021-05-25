# Auto Saver

[JunctionX Seoul 2021 해커톤 참가](https://app.hackjunction.com/events/junctionx-seoul-2021)

운전 중 교통 사고 감지 앱

- 진행기간 : 2021. 05. 21 ~ 2021. 05. 23
- 사용기술 : Kotlin, Java, OkHttp, MPAndroidChart, MVVM

<p align="center"><img width="30%" src="https://user-images.githubusercontent.com/55052074/119483057-8e482d80-bd8f-11eb-97ba-e53455e392c9.png"/> <img width="30%" src="https://user-images.githubusercontent.com/55052074/119483068-91431e00-bd8f-11eb-98fb-f312d12c16e9.png"/></p>


## 서비스 소개

- Auto Saver는 운전 시 기기의 흔들림을 감지하여 교통사고 또는 몸이 불편하신 분들의 낙상사고 시 빠른 대처가 가능하도록 도와주는 앱입니다.
- 운전 중 데이터를 수집해 서버에 저장하고 그래프 형태로 표시합니다.
- 운전 중 충격, 흔들림이 감지되면 경고를 띄우고 119와 비상연락망에 전화와 문자를 전송합니다.

## 상세 기능 소개

### 1. 회원가입

### 1-1. 회원가입

<p align="center"><img width="30%" src="https://user-images.githubusercontent.com/55052074/119483234-b768be00-bd8f-11eb-9f66-50901936741c.png"/> <img width="30%" src="https://user-images.githubusercontent.com/55052074/119483242-b9328180-bd8f-11eb-85a7-657e95ccfa61.png"/></p>

- 로그인 화면에서 **회원가입** 선택시 해당 화면으로 이동하며, 이메일과 패스워드로 가입이 가능합니다.
- NEXT를 누르면 서버에서 유효성 검사를 진행하고 결과값을 표시합니다.
- 패스워드는 HASH로 암호화되어 전송됩니다.

### 1-2. 상세정보

<p align="center"><img width="30%" src="https://user-images.githubusercontent.com/55052074/119483375-dc5d3100-bd8f-11eb-8bbc-28d49794c706.png"/> <img width="30%" src="https://user-images.githubusercontent.com/55052074/119483382-debf8b00-bd8f-11eb-940a-389105ff5545.png"/></p>

- NEXT 버튼을 클릭하면 상세 정보 입력 화면으로 넘어옵니다.
- 혈액형, 특이사항은 드롭다운 메뉴로, 병력 사항과 복용 약물은 텍스트 형태로 입력받습니다.

### 1-3. 비상 연락망

<p align="center"><img width="30%" src="https://user-images.githubusercontent.com/55052074/119483469-f6970f00-bd8f-11eb-946f-841657561c2a.png"/> <img width="30%" src="https://user-images.githubusercontent.com/55052074/119483478-f8f96900-bd8f-11eb-9ddd-6e589fce0e4f.png"/></p>

- COMPLETION 버튼을 클릭하면 비상연락망 입력 화면으로 넘어옵니다.
- 이름과 휴대폰 번호를 입력받습니다.
- SKIP 버튼을 누르면 정보를 저장하지 않고 넘어갑니다.
- COMPLETION을 누르면 상세 정보와 함께 서버에 전송합니다.
- 해당 과정까지 완료하지 않고 앱을 종료할 시 다음번 로그인에서 다시 입력받습니다.

### 2. 로그인

<p align="center"><img width="30%" src="https://user-images.githubusercontent.com/55052074/119483566-11698380-bd90-11eb-9265-b3a874f3a27a.png"/></p>

- 이메일과 패스워드를 사용해 로그인이 가능합니다.
- Auto Log-in을 활성화하고 로그인 시 다음 번 앱을 실행하면 자동으로 로그인 됩니다.

### 3. 운전 습관 차트

<p align="center"><img width="30%" src="https://user-images.githubusercontent.com/55052074/119483619-1f1f0900-bd90-11eb-9f44-16c38c97a696.png"/></p>

- 서버에 저장된 데이터를 차트 형태로 표시합니다.
- 가속도 센서 정보를 X축, Y축, Z축으로 나누어 그래프를 표시합니다.

### 4. 운전 중 사고 감지

<p align="center"><img width="30%" src="https://user-images.githubusercontent.com/55052074/119483695-33630600-bd90-11eb-873f-3987722af013.jpg"/></p>

- 가속도 센서를 이용하여 충격, 흔들림을 감지합니다.
- 큰 충격이나 흔들림이 감지되면 사고가 난 것으로 인식합니다.
- 가속도 데이터는 실시간으로 서버로 전송되어 저장됩니다.

### 4-1. 사고 감지 시작

<p align="center"><img width="30%" src="https://user-images.githubusercontent.com/55052074/119483765-44137c00-bd90-11eb-86b3-9ae1ddd49a3a.jpg"/></p>

- START 버튼을 누르면 운전 중 센서 감지가 시작됩니다.
- 가속도 센서 데이터가 X축, Y축, Z축으로 나뉘어 서버에 저장됩니다.
- END OF DRIVE를 누르면 센서 감지가 종료됩니다.

### 4-2. 사고 감지

<p align="center"><img width="30%" src="https://user-images.githubusercontent.com/55052074/119483816-568db580-bd90-11eb-9555-5424a9a803be.png"/> <img width="30%" src="https://user-images.githubusercontent.com/55052074/119483827-5a213c80-bd90-11eb-8495-2e4e27dc2c61.png"/> <img width="30%" src="https://user-images.githubusercontent.com/55052074/119484012-918fe900-bd90-11eb-808e-ea253f0e28a9.png"/></p>


- 충격이나 강한 흔들림이 감지되면 사고로 인식합니다.
- 10초 카운트 후 119, 비상연락망에게 문자를 보내고 119에 전화를 겁니다.
- 문자에는 현재 시각, 현재 위치의 위경도, 혈액형 정보를 포함합니다.
- 10초가 카운트되는 동안 CANCEL 버튼을 누르거나 GPS 위치 변동, 움직임이 감지되면 사고 감지가 해제됩니다.

### 5. 마이페이지

<p align="center"><img width="30%" src="https://user-images.githubusercontent.com/55052074/119484084-9fde0500-bd90-11eb-8f00-220053ac8cf8.png"/> <img width="30%" src="https://user-images.githubusercontent.com/55052074/119484095-a3718c00-bd90-11eb-9a49-8489dbf39032.png"/></p>

- 각 화면에서 상세 정보와 비상연락망 정보 변경이 가능합니다.

## 보완사항

- 운전 습관 데이터 분석 체계화

## 기타사항

- MVVM 패턴 적용
