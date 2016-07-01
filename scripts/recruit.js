// 프리셋으로 지정한 유닛을 자동으로 뽑는다.
// 각 마을마다 뽑고 싶은 유닛을 프리셋으로 지정해야 한다.
// 이 때 프리셋 이름에 다음 글자가 포함되어야 실행한다: rec

window.socketService = window.injector.get('socketService');
window.routeProvider = window.injector.get('routeProvider');

var village_list = [];
var presets_recruit = [];

console.log("It's recruiting time!");


// 현재 플레이어가 소유한 마을의 list
function make_village_list(){
    console.log("Make village list");
    socketService.emit(routeProvider.CHAR_GET_INFO, {}, function(data){
        for(i=0; i < data.villages.length; i++){
            console.log(data.villages[i].name);
            console.log(data.villages[i].villageId);
            village_list.push(data.villages[i].villageId);            
        }
    });
    // 5초 후에 make_preset_list 함수 실행
    setTimeout(make_preset_list, 5000);
}

// 리크루팅에 사용할 preset의 list
function make_preset_list(){
    socketService.emit(routeProvider.GET_PRESETS, {}, function(data){
        for(i=0; i < data.presets.length; i++){
            // rec 단어 파싱
            if ((data.presets[i].name).indexOf("rec") >= 0){
                console.log(data.presets[i].name);
                console.log(data.presets[i].id);
                presets_recruit.push(data.presets[i].id);
            }
        }
    });
    // 5초 후에 recruit 함수 실행
    setTimeout(recruit, 5000);
}

function recruit(){
    for (i=0 ; i < village_list.length; i++){
        for (j=0; j < presets_recruit.length; j++){
            socketService.emit(routeProvider.RECRUIT_PRESET, {
                village_id: village_list[i],
		preset_id: presets_recruit[j]
	    });
        }
    }
}

setTimeout(make_village_list, 5000);
