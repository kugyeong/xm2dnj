
window.socketService = window.injector.get('socketService');
window.routeProvider = window.injector.get('routeProvider');
var village_id_list = [];
var village_name_list = [];
var archer = 0
var axe = 0
var catapult = 0
var bersekers = 0
var hc = 0
var paladin = 0
var lc = 0
var mounted_archer = 0
var ram = 0
var noble = 0
var spear = 0
var sword = 0
var trebuchet = 0

setTimeout(make_village_list, 1000);
function make_village_list(){
    console.log("Make village list");
    socketService.emit(routeProvider.GET_CHARACTER_VILLAGES, {}, function(data){
        for(i=0; i < data.villages.length; i++){
            console.log(data.villages[i].name);
            console.log(data.villages[i].id);
            village_name_list.push(data.villages[i].name);
            village_id_list.push(data.villages[i].id);
         }
    });
    
    setTimeout(tel_troepen, 4000);
}

function tel_troepen(){

  for(i=0; i < village_id_list.length; i++){
    socketService.emit(routeProvider.VILLAGE_UNIT_INFO, {village_id: village_id_list[i]}, function(data){
      var need = 0;
      axe += data.available_units.axe.total;
      archer += data.available_units.archer.total;
      catapult += data.available_units.catapult.total;
      bersekers += data.available_units.doppelsoldner.total;
      hc += data.available_units.heavy_cavalry.total;
      paladin += data.available_units.knight.total;
      lc += data.available_units.light_cavalry.total;
      mounted_archer += data.available_units.mounted_archer.total;
      ram += data.available_units.ram.total;
      noble += data.available_units.snob.total;
      spear += data.available_units.spear.total;
      sword += data.available_units.sword.total;
      trebuchet += data.available_units.trebuchet.total;
      if(data.available_units.spear.total < 5000  ) {
        console.log("Less than 4000 : ");
        for(j=0; j < village_id_list.length; j++) {
          if(village_id_list[j] == data.village_id) {
            console.log(village_name_list[j]);
          }
        }
        if(data.queues.barracks[0].unit_type == "spear")
          need = 5000 - (data.available_units.spear.total + data.queues.barracks[0].amount - data.queues.barracks[0].recruited);
        else
          need = 5000 - (data.available_units.spear.total);
        console.log("Need : "+need);
      }
      
      if(data.available_units.spear.total > 5000 ) {
        console.log("More than 4000 : ");
        for(j=0; j < village_id_list.length; j++) {
          if(village_id_list[j] == data.village_id) {
            console.log(village_name_list[j]);
          }
        }
        console.log(data);
      }
      
    });
  }
  setTimeout(show_troepen, 2000)
}

function show_troepen(){
  console.log("Total defender: ")
  console.log("spear: "+spear+", sword: "+sword+", archer: "+archer+", hc: "+hc+", trebuchet: "+trebuchet)
  console.log("Total striker: ")
  console.log("axe: "+axe+", lc: "+lc+", mounted_archer: "+mounted_archer+", ram: "+ram+", catapult: "+catapult+", bersekers: "+bersekers)
  console.log("Total specialist:")
  console.log("Paladin: "+paladin+", noble: "+noble)
}
