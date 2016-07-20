// 건물 자동 건설 스크립트
// 시작 index와 종료 index를 지정하여 마을 10개 단위로 나누어 실행한다.
var begin_vill = 0;
var end_vill = 10;

window.socketService = window.injector.get('socketService');
window.routeProvider = window.injector.get('routeProvider');

var village_list = [];
var building_list = ["academy", "timber_camp", "clay_pit", "iron_mine", "headquarter", "barracks",
                  "warehouse", "farm", "tavern", "hospital", "preceptory",
                  "church", "chapel", "rally_point", "statue", "market", "wall"];

var HQ_ACDM = 20;
var HQ_PREC = 25;
var MAX_ACDM = 1;
var MAX_PRECEP = 10;
var MAX_FARM = 30;
var MAX_WAREHOUSE = 30;
var MAX_WALL = 20;
var MAX_RALLY = 5;
var MAX_STATUE = 5;
var BARRACKS_HC = 21;
var MAX_MARKET = 25;


function make_village_list(){
    socketService.emit(routeProvider.GET_CHARACTER_VILLAGES, {}, function(data){
        for(i=begin_vill; i < end_vill; i++){
            console.log(data.villages[i].name)
            console.log(data.villages[i].id)
            village_list.push(data.villages[i].id)
        }
    });
    setTimeout(buildBuildings, 5000);
}

function buildBuildings(){

  for(i=0; i < village_list.length; i++){

    socketService.emit(routeProvider.VILLAGE_GET_VILLAGE, {village_id: village_list[i]}, function(data){
      console.log(data.resources)

      for(j=0; j < village_list.length; j++){

        if(data.buildings["wall"].level < MAX_WALL){
          socketService.emit(routeProvider.VILLAGE_UPGRADE_BUILDING, {
            village_id: village_list[j], building: "wall", premium: 0, location: 0}, function(data){});
        }
        if(data.resources.food < 1000 && data.buildings["farm"].level < MAX_FARM){
          console.log("We need farm!");
          socketService.emit(routeProvider.VILLAGE_UPGRADE_BUILDING, {
            village_id: village_list[j], building: "farm", premium: 0, location: 0}, function(data){});       
        }
        if(data.buildings["warehouse"].level < MAX_WAREHOUSE){
          socketService.emit(routeProvider.VILLAGE_UPGRADE_BUILDING, {
            village_id: village_list[j], building: "warehouse", premium: 0, location: 0}, function(data){});
        }
        if(data.buildings["barracks"].level < BARRACKS_HC){
          socketService.emit(routeProvider.VILLAGE_UPGRADE_BUILDING, {
            village_id: village_list[j], building: "barracks", premium: 0, location: 0}, function(data){});
        }
        if(data.buildings["rally_point"].level < MAX_RALLY){
          socketService.emit(routeProvider.VILLAGE_UPGRADE_BUILDING, {
            village_id: village_list[j], building: "rally_point", premium: 0, location: 0}, function(data){});
        }
        if(data.buildings["statue"].level < MAX_STATUE){
          socketService.emit(routeProvider.VILLAGE_UPGRADE_BUILDING, {
            village_id: village_list[j], building: "statue", premium: 0, location: 0}, function(data){});
        }
        if(data.buildings["headquarter"].level < HQ_PREC){
          socketService.emit(routeProvider.VILLAGE_UPGRADE_BUILDING, {
            village_id: village_list[j], building: "headquarter", premium: 0, location: 0}, function(data){});
        }
        if(data.buildings["headquarter"].level >= HQ_ACDM && data.buildings["academy"].level < MAX_ACDM){
          socketService.emit(routeProvider.VILLAGE_UPGRADE_BUILDING, {
            village_id: village_list[j], building: "academy", premium: 0, location: 0}, function(data){});
        }
        if(data.buildings["headquarter"].level >= HQ_PREC && data.buildings["preceptory"].level < MAX_PRECEP){
          socketService.emit(routeProvider.VILLAGE_UPGRADE_BUILDING, {
            village_id: village_list[j], building: "preceptory", premium: 0, location: 0}, function(data){});
        }
        if(data.buildings["market"].level < MAX_MARKET){
          socketService.emit(routeProvider.VILLAGE_UPGRADE_BUILDING, {
            village_id: village_list[j], building: "market", premium: 0, location: 0}, function(data){});
        }

      }
    });
  }

}

console.log("It's BUILDING time! ^^");
setTimeout(make_village_list, 5000);

