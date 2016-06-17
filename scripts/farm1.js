// ==UserScript==
// @name Memory Killer
// @namespace
// @include
// @version     1
// @grant
// ==/UserScript==


// 자동 약탈 스크립트
// 주의: preset 이름에 farm 이라는 단어가 있어야 파싱해서 약탈을 보냄
// 예를 들어서
// spear farm 이라고 이름을 붙인 뒤 해당 preset에 창병 100을 설정해두면 한 번에 창병 100명씩 약탈을 보냄
// 참고로
// 한 마을에서 보낼 수 있는 공격은 최대 50건으로 규정되어 있음

// 마을이 10개를 넘어가면 스크립트가 제대로 실행되지 않고 뻗음
// 10개씩 나눠서 스크립트를 돌려야 안정적으로 운영됨
var begin_vill = 0;
var end_vill = 10;

// 약탈 기준점 좌표를 결정하기 위한 값
// (-10,-10)으로 설정하면, 자신의 마을을 정중앙으로 기준을 삼고 사방으로 고르게 약탈을 보냄 (기본값)
// 예를 들어 (5,5)로 설정하면, 자신의 마을보다 오른쪽 아래를 기준점으로 삼고 그 좌표를 기준으로 사방으로 약탈을 보냄
var relative_x = -10;
var relative_y = -10;


window.socketService = window.injector.get('socketService');
window.routeProvider = window.injector.get('routeProvider');

// 약탈 대상 (바바리안, 즉 회색 마을)
var barbfarm = [];
// 공격에 사용할 preset
var farmpresets = [];
// 내 마을의 list 및 좌표
var village_list = [];
var village_loc_x = [];
var village_loc_y = [];
// 공격 대상 좌표 (make_farm_list 함수에서 사용)
var attack_x = 0;
var attack_y = 0;

// 몇 개의 바바리안 마을을 타겟으로 하는지 확인 용도
var num_barbs = 0;




// 스크립트 시작
console.log("It's PARTY time!");

// 5초 후에 make_village_list 함수 실행
setTimeout(make_village_list, 5000);

// 현재 플레이어가 소유한 마을의 list
function make_village_list(){
    console.log("Make village list");
    socketService.emit(routeProvider.CHAR_GET_INFO, {}, function(data){
        for(i=begin_vill; i < end_vill; i++){
            console.log(data.villages[i].name);
            console.log(data.villages[i].villageId);
            // village_list는 내 모든 마을의 ID를 저장하는 list
            village_list.push(data.villages[i].villageId);
            village_loc_x.push(data.villages[i].x);
            village_loc_y.push(data.villages[i].y);
        }
    });
    // 5초 후에 make_preset_list 함수 실행
    setTimeout(make_preset_list, 5000);
}

// 추후 farming에 사용할 preset의 list
function make_preset_list(){
    socketService.emit(routeProvider.GET_PRESETS, {}, function(data){
        for(i=0; i < data.presets.length; i++){
            // farm이라는 단어를 파싱
            if ((data.presets[i].name).indexOf("farm") >= 0){
                console.log(data.presets[i].name);
                console.log(data.presets[i].id);
                farmpresets.push(data.presets[i].id);
            }
        }
    });
    // 5초 후에 make_farm_list 함수 실행
    setTimeout(make_farm_list, 5000);
}

// 내 마을 주위의 바바리안 마을 ID를 barbfarm 배열에 저장
function make_farm_list(){
    // 초기화 (바바리안 마을을 방금 누군가 노블했을 수도 있기 때문에 매번 다시 확인함)
    barbfarm = [];
    for (i=0, num_barbs=0; i < village_list.length; i++){

        // 내가 소유한 각 마을을 기준으로 20*20 범위의 모든 마을을 탐색
        // 좌표에서 (-10,-10) 해줘야 정 중앙으로 계산됨

        attack_x = village_loc_x[i]+relative_x;
        attack_y = village_loc_y[i]+relative_y;

        socketService.emit(routeProvider.MAP_GETVILLAGES, {x:(attack_x), y:(attack_y), width:20, height:20}, function(data){
            for (j = 0; j < data.villages.length; j++){
                // character_name이 null이면 바바리안 (회색 마을)
                if (data.villages[j].character_name === null){
                    console.log(data.villages[j]);
                    barbfarm.push(data.villages[j].id);
                    num_barbs++;
                    console.log("Num of barbs: [%i]", num_barbs);
                }
            }
        });
    }
    // 10초 후에 send_farm_list 함수 실행
    setTimeout(send_farm_list, 10000);
}

// 약탈 보냄
function send_farm_list(){
    for (i=0 ; i < village_list.length; i++){
        for (j=0; j < farmpresets.length; j++){
            for (k=0; k < barbfarm.length; k++){
                try{
                    socketService.emit(routeProvider.SEND_PRESET, {start_village: village_list[i], target_village: barbfarm[k], army_preset_id: farmpresets[j], type: 'attack'}, function(data){
                        // 공격 보낸 곳은 list에서 제거
                        barbfarm.splice(0, 1);
                    });
                }
                catch(err){
                    console.log("No more troops!");
                    break;
                }
            }
        }
    }
}

