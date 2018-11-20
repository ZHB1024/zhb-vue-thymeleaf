//上个月的日期
function getPreDate(){
	var nowdays = new Date();
	var year = nowdays.getFullYear();
	var month = nowdays.getMonth();
	if(month==0){
		month=11;
        year=year-1;
	}else{
		month -=1;
		if (month < 10) {
			month = "0" + month;
		}
	}
	return new Date(year,month,"01");
}