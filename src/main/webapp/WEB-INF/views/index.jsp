<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h3>단일 파일 업로드1</h3>
	<!-- 
		multipart/form-data
		- 모든 문자를 인코딩하지 않고 보내는 것을 명시할 때 쓰는 명령어
		- 기존 application/x-www-form-urlencoded는 기존 문자들을 서버로 보내기전에 인코딩을 한다.
		- 이 과정에서 바이너리로 보내게 될 데이터를 보낼 수가 없게 되고 type="file"로 처리되는 데이터는 서버로
		  전송이 불가능하다.
		- enctype을 multipart/form-data로 변경하게 되면 기존의 데이터 뿐만 아니라 바이너리 데이터까지 전송이
		  가능해진다. 
	 -->
	<form action="${pageContext.request.contextPath}/test1" method="post" enctype="multipart/form-data">
		<ul>
			<li><h4>select File</h4></li>
			<li><input type="file" name="mediaFile"/></li>
			<li><button>Upload</button></li>
		</ul>
	</form>
	
	<h3>다중 파일 업로드</h3>
	<form action="${pageContext.request.contextPath}/test2" method="post" enctype="multipart/form-data">
		<ul>
			<li><h4>select File</h4></li>
			<!-- multiple을 쓰면 파일을 여러 개 선택할 수 있었다. -->
			<li><input type="file" name="mediaFile" multiple/></li>
			<li><button>Upload</button></li>
		</ul>
	</form>
	
	<h3>파일 업로드 + 추가 정보</h3>
	<form action="${pageContext.request.contextPath}/test3" method="post" enctype="multipart/form-data">
		<ul>
			<li><h4>select File</h4></li>
			<li><label for="user">user : </label><input type="text" name="user" id="user"/></li>
			<li><label for="url">url : </label><input type="text" name="url" id="url"/></li>
			<li><input type="file" name="mediaFile" multiple/></li>
			<li><button>Upload</button></li>
		</ul>
	</form>
	
	<h3>파일 업로드 + 추가 정보2</h3>
	<form action="${pageContext.request.contextPath}/test4" method="post" enctype="multipart/form-data">
		<ul>
			<li><h4>select File</h4></li>
			<li><label for="user">user : </label><input type="text" name="user" id="user"/></li>
			<li><label for="url">url : </label><input type="text" name="url" id="url"/></li>
			<li><input type="file" name="mediaFile" multiple/></li>
			<li><button>Upload</button></li>
		</ul>
	</form>
	
	<!-- 파일 보안 모듈을 별도로 태워야 한다면 비동기 통신을 이용해라 -->
	<h3>비동기 통신 업로드</h3>
	<div class="uploadDiv">
		<input type="file" name="mediaFile" id="mediaFile" multiple/>
		<button id="btn">Upload</button>
	</div>


	<script type="text/javascript">
		document.getElementById("btn").addEventListener("click",function(){
			// formData를 통해 데이터를 보낼 양식 설정
			const formData = new FormData();
			// mediaFile input = file 태그 엘리먼트를 선언
			const inputFiles = document.getElementById("mediaFile");
			// inputFiles에서 파일에 대한 정보들을 전부 가져와 변수에 저장
			let files = inputFiles.files;
			// 어떤 값이 오는지 임시 출력
			console.log(files);
			
			// files의 정보를 formData에 담기
			for(const file of files){
				formData.append("uploadFile", file);
			}
			
			// fetch를 통해 formData 전송 - 메서드는 반드시 post가 되어야 한다.
			fetch("${pageContext.request.contextPath}/test5", {
				method : "POST",
				body : formData})
			.then(response => console.log(response))
			.catch(error => console.log(error));
		});
	</script>	
	
	<hr />
	<h1>파일 다운로드</h1>
	<a href="${pageContext.request.contextPath}/download1">다운로드</a>
	<!-- 외부에 있는 파일을 불러오고 싶을 때 - ServletAppContext 경로를 수정해줘야 함. -->
	<img src="upload/muffin.png" />
	
</body>
</html>