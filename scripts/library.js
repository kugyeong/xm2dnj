window.socketService = window.injector.get('socketService');
window.routeProvider = window.injector.get('routeProvider');

function printEnd(){
    console.log("!!!!!!!!!!!!!!Process ends!!!!!!!!!!!!!!!!!!!!!!!");
}

function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;    
    }
  }
}

function getRandomInt(min, max) {
  return Math.floor(Math.random() * (max - min)) + min;
}

function isOdd(num) { return num % 2;}

function spearTimeTravel(x1,y1,x2,y2) {
    if(!isOdd(y1))
        x1 = x1 - 0.5;
    if(!isOdd(y2))
        x2 = x2 - 0.5;
    return 0.2 * 14 * Math.sqrt((x1-x2)*(x1-x2) + 0.75*(y1-y2)*(y1-y2));
}

function trading_send_resources(s_vil, t_vil, w, c, i) {
    socketService.emit(routeProvider.TRADING_SEND_RESOURCES, {start_village: s_vil, target_village: t_vil, wood: w, clay: c, iron: i}, function(data){
    });
}

function preceptory_recruit(vil, u_type, u_amount) {
    socketService.emit(routeProvider.PRECEPTORY_RECRUIT, {village_id: vil, unit_type: u_type, amount: u_amount}, function(data){
        //barbfarm.splice(0, 1);
    });    
}

function barrack_recruit(vil, u_type, u_amount) {
    socketService.emit(routeProvider.BARRACKS_RECRUIT, {village_id: vil, unit_type: u_type, amount: u_amount}, function(data){
        //barbfarm.splice(0, 1);
    });    
}

function village_get_village(vil) {
    socketService.emit(routeProvider.VILLAGE_GET_VILLAGE, {village_id: vil}, function(data){
        //barbfarm.splice(0, 1);
        //make xml
    });    
}

function mint_coins(vil, m_amount) {
    /*
    socketService.emit(routeProvider.MINT_COINS, {village_id: vil, amount: m_amount}, function(data){   
    })
    */ 
    
    var res = m_amount * 25000;
    socketService.emit(routeProvider.TRIBE_SKILL_DONATE, {village_id:vil, crowns:0, resources:{'wood':res, 'clay':res, 'iron':res}}, function(data){
    });
    
}

function send_custom_army(s_vil, t_vil, a_type, a_units, c_target, a_officers, a_icon) {
    socketService.emit(routeProvider.SEND_CUSTOM_ARMY, {start_village: s_vil, target_village: t_vil, type: a_type, units: a_units, catapult_target: c_target, officers: a_officers, icon: a_icon}, function(data){
    });
}

function vil_build(vil, b) {
    socketService.emit(routeProvider.VILLAGE_UPGRADE_BUILDING, {
            village_id: vil, building: b, premium: 0, location: 0}, function(data){});
}

(function(console){

console.save = function(data, filename){

    if(!data) {
        console.error('Console.save: No data')
        return;
    }

    if(!filename) filename = 'console.json'

    if(typeof data === "object"){
        data = JSON.stringify(data, undefined, 4)
    }

    var blob = new Blob([data], {type: 'text/json'}),
        e    = document.createEvent('MouseEvents'),
        a    = document.createElement('a')

    a.download = filename
    a.href = window.URL.createObjectURL(blob)
    a.dataset.downloadurl =  ['text/json', a.download, a.href].join(':')
    e.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null)
    a.dispatchEvent(e)
 }
})(console)


var vil_info = "";
function write_vil_info_file(vil) {
    vil_info = "";
    socketService.emit(routeProvider.VILLAGE_GET_VILLAGE, {village_id: vil}, function(data){
        vil_info += ",id:"+data.villageId;
        vil_info += ",x:"+data.x;
        vil_info += ",y:"+data.y;
        vil_info += ",base_storage:"+data.base_storage;
        vil_info += ",academy_lvl:"+data.buildings.academy.level;
        vil_info += ",barracks_lvl:"+data.buildings.barracks.level;
        vil_info += ",chapel_lvl:"+data.buildings.chapel.level;
        vil_info += ",church_lvl:"+data.buildings.church.level;
        vil_info += ",clay_pit_lvl:"+data.buildings.clay_pit.level;
        vil_info += ",farm_lvl:"+data.buildings.farm.level;
        vil_info += ",headquarter_lvl:"+data.buildings.headquarter.level;
        vil_info += ",hospital_lvl:"+data.buildings.hospital.level;
        vil_info += ",iron_mine_lvl:"+data.buildings.iron_mine.level;
        vil_info += ",market_lvl:"+data.buildings.market.level;
        vil_info += ",rally_point_lvl:"+data.buildings.rally_point.level;
        vil_info += ",preceptory_lvl:"+data.buildings.preceptory.level;
        vil_info += ",statue_lvl:"+data.buildings.statue.level;
        vil_info += ",tavern_lvl:"+data.buildings.tavern.level;
        vil_info += ",timber_camp_lvl:"+data.buildings.timber_camp.level;
        vil_info += ",wall_lvl:"+data.buildings.wall.level;
        vil_info += ",warehouse_lvl:"+data.buildings.warehouse.level;
        vil_info += ",res_clay:"+data.resources.clay;
        vil_info += ",res_wood:"+data.resources.wood;
        vil_info += ",res_iron:"+data.resources.iron;
        vil_info += ",res_food:"+data.resources.food;
        vil_info += ",preceptory_order:"+data.preceptory_order;
        vil_info += ",loyalty:"+parseInt(data.loyalty);

        console.log(data.name);
        /*
        console.log(vil_info);
        console.log(data.preceptory_order);
        console.log(data.buildings.academy.level);
    console.log(data.buildings.barracks.level);
    console.log(data.buildings.chapel.level);
    console.log(data.buildings.church.level);
    console.log(data.buildings.clay_pit.level);
    console.log(data.buildings.farm.level);
    console.log(data.buildings.headquarter.level);
    console.log(data.buildings.hospital.level);
    console.log(data.buildings.iron_mine.level);
    console.log(data.buildings.market.level);
    console.log(data.buildings.rally_point.level);
    console.log(data.buildings.preceptory.level);
    console.log(data.buildings.statue.level);
    console.log(data.buildings.tavern.level);
    console.log(data.buildings.timber_camp.level);
    console.log(data.buildings.wall.level);
    console.log(data.buildings.warehouse.level);


    console.log(data);

    console.log(data.resources.clay);
    console.log(data.resources.food);
    console.log(data.resources.iron);
    console.log(data.resources.wood);
    */
    });
    socketService.emit(routeProvider.VILLAGE_UNIT_INFO, {village_id: vil}, function(data){
      console.log(data);
      
      vil_info += ",axe:"+data.available_units.axe.in_town;
      vil_info += ",archer:"+data.available_units.archer.in_town;
      vil_info += ",catapult:"+data.available_units.catapult.in_town;
      vil_info += ",bersekers:"+data.available_units.doppelsoldner.in_town;
      vil_info += ",heavy_cavalry:"+data.available_units.heavy_cavalry.in_town;
      vil_info += ",paladin:"+data.available_units.knight.in_town;
      vil_info += ",light_cavalry:"+data.available_units.light_cavalry.in_town;
      vil_info += ",mounted_archer:"+data.available_units.mounted_archer.in_town;
      vil_info += ",ram:"+data.available_units.ram.in_town;
      vil_info += ",noble:"+data.available_units.snob.in_town;
      vil_info += ",spear:"+data.available_units.spear.in_town;
      vil_info += ",sword:"+data.available_units.sword.in_town;
      vil_info += ",trebuchet:"+data.available_units.trebuchet.in_town;
      vil_info += ",barrack_queue_len:"+data.queues.barracks.length;
      /*
      console.log(vil_info);
      console.log(data.queues.barracks.length);
      */
    }); 
    socketService.emit(routeProvider.VILLAGE_GET_BUILDING_QUEUE, {village_id: vil}, function(data){
    vil_info += ",build_queue_len:"+data.queue.length;
    //console.log(data.queue.length);
    //console.log(data.queue[0].building);
    //console.log(data.queue[0].level);
    }) ;
    socketService.emit(routeProvider.TRADING_GET_MERCHANT_STATUS, {village_id: vil}, function(data){
      vil_info += ",total_merchant:"+data.total;
      vil_info += ",free_merchant:"+data.free;
      vil_info += ",busy_merchant:"+data.busy;
      //console.log(data.total);
      //console.log(data.free);
      //console.log(data.busy);
      //console.log(vil_info);
    });
    setTimeout(write_file, 5000, vil);
}

var global_info = "";
var village_list = "";
var vil_ids = [];
function make_village_list(){
    village_list = "";
    console.log("Make village list");
    socketService.emit(routeProvider.GET_CHARACTER_VILLAGES, {}, function(data){
        for(i=0; i < data.villages.length; i++){
            console.log(data.villages[i].name);
            console.log(data.villages[i].id);
            console.log(data.villages[i]);
            if ((data.villages[i].name).indexOf("E_") >= 0) {
                continue;
            } else {
                village_list += data.villages[i].id+",";
                vil_ids.push(data.villages[i].id);
            }
        }
        village_list = village_list.substr(0, village_list.length -1);

    });
    
    setTimeout(write_list_file, 5000);
    setTimeout(write_global_file, 5100, vil_ids);
}

function write_global_file(vil_ids) {
    global_info = "";
    socketService.emit(routeProvider.VILLAGE_BATCH_GET_VILLAGE_DATA, {village_ids:vil_ids}, function(data){
        for(i=0; i < vil_ids.length; i++){
            var id_str = vil_ids[i].toString();
            global_info += "vil_id:"+data[id_str]["Village/village"].villageId;
            global_info += ",req_wood:0";
            global_info += ",req_clay:0";
            global_info += ",req_iron:0";
            global_info += ",remain_food:"+data[id_str]["Village/village"]["resources"].food;
            global_info += ",has_academy:"+data[id_str]["Village/village"]["buildings"]["academy"].level;
            global_info += ",x:"+data[id_str]["Village/village"].x;
            global_info += ",y:"+data[id_str]["Village/village"].y;
            global_info += ",loyalty:"+parseInt(data[id_str]["Village/village"].loyalty);
            global_info += ",recruit:1\n";
            
        }
        //village_list = village_list.substr(0, village_list.length -1);

    });
    setTimeout(write_global_file2, 10000);
}

function write_list_file() {
    console.save(village_list, "village_list.txt");
}

function write_global_file2() {
    console.save(global_info, "global.txt");
}

function write_file(vil) {
    console.save(vil_info, vil+".txt");
}

farming_units = {'spear':9999,'sword':9999,'archer':9999,'axe':9999,'light_cavalry':9999,'mounted_archer':9999,'ram':0,'catapult':0,'heavy_cavalry':9999};
farming_officers = {'bastard':false, 'leader':false, 'loot_master':false, 'medic':false, 'scout':false, 'supporter':false};
var mark = [];
var barbfarm = [];
var preset_id;
var farming_count = 0;
var count_per_farming;
function get_barb_list(vil,attack_x,attack_y, units) {
    //attack_x += (getRandomInt(0, 20) - 10);
    //attack_y += (getRandomInt(0, 20) - 10);
    socketService.emit(routeProvider.MAP_GETVILLAGES, {x:(attack_x), y:(attack_y), width:20, height:20}, function(data){
        for (j = 0; j < data.villages.length; j++){
            // character_name이 null이면 바바리안 (회색 마을)
            if (data.villages[j].character_name === null && data.villages[j].points > 3000){
                //console.log(data.villages[j]);
                barbfarm.push(data.villages[j].id);
                //num_barbs++;
                //console.log("Num of barbs: [%i]", num_barbs);
            }
        }
    });
    setTimeout(shuffle, 3000, vil, barbfarm, units);
}

function shuffle(vil, array, units) {
  var currentIndex = array.length, temporaryValue, randomIndex;

  // While there remain elements to shuffle...
  while (0 !== currentIndex) {

    // Pick a remaining element...
    randomIndex = Math.floor(Math.random() * currentIndex);
    currentIndex -= 1;

    // And swap it with the current element.
    temporaryValue = array[currentIndex];
    array[currentIndex] = array[randomIndex];
    array[randomIndex] = temporaryValue;
  }
  setTimeout(save_new_preset, 1000 ,vil, "temp_farming", 65805, farming_officers, units);
}

function save_new_preset(vil,p_name,p_icon,p_officers,p_units) {
    socketService.emit(routeProvider.SAVE_NEW_PRESET, {village_id:vil, name:p_name, icon:p_icon, officers:p_officers, units:p_units}, function(data){
        console.log(data);
        preset_id = data.id;
    });
    setTimeout(send_farm_preset, 1000 ,vil);
}
function delete_preset(p_id) {
    socketService.emit(routeProvider.DELETE_PRESET, {id:p_id}, function(data){
        console.log(data);
    });
}
//delete_preset(12708699)
//save_new_preset(12733, "temp_farming", 65805, farming_officers, farming_units)
function get_preset(vil) {
    socketService.emit(routeProvider.GET_PRESETS_FOR_VILLAGE, {village_id:vil}, function(data){
        for (j=0; j<data.presets.length; j++) {
            if ((data.presets[j].name).indexOf("farm") >= 0){               
                preset_id = data.presets[j].id;        
            }
        }
        console.log(data);
    });
    setTimeout(send_farm_preset, 1000 ,vil);
}

function send_farm_preset(vil) {
    // get unmarked barb id
    var idx;
    var avail_barb = [];
    for (idx=0; idx < barbfarm.length; idx++) {
        var isExist =false;
        for( j = 0; j<mark.length; j++) {
            if( mark[j] == barbfarm[idx] ){
                isExist = true;
                break;
            }
        }
        if (isExist == false) {
            avail_barb.push(barbfarm[idx]);
        }
        if (avail_barb.length == count_per_farming) {
            break;
        }
    }
    if(preset_id != -1) {
        for (j=0; j<avail_barb.length; j++) {
            socketService.emit(routeProvider.SEND_PRESET, {start_village: vil, target_village: avail_barb[j], army_preset_id: preset_id, type: 'attack'}, function(data){
                //barbfarm.splice(0, 1);
            });
            mark.push(avail_barb[j]);
            farming_count++;
        }
    }
    // mark that barb id
    if (farming_count > 1000 || avail_barb.length == 0)
    {
        var shift_count = parseInt(farming_count / 15);
        for (i=0; i<shift_count; i++)
            mark.shift();
        farming_count -= shift_count;
    }

    setTimeout(delete_preset, 3000 ,preset_id);
}

function go_farm(vil,attack_x,attack_y,units) {
    //count_per_farming = parseInt(units['spear'] / farming_units['spear']);
    //if(count_per_farming > 20)
    //    count_per_farming = 20;
    barbfarm = [];
    preset_id = -1;
    var total = units['spear'] + units['sword'] + units['archer'] + units['axe'] + units['heavy_cavalry'];
    units['spear'] = parseInt(units['spear']);
    units['sword'] = parseInt(units['sword']);
    units['archer'] = parseInt(units['archer']);
    units['axe'] = parseInt(units['axe']);
    units['light_cavalry'] = 0;
    units['mounted_archer'] = 0;
    units['heavy_cavalry'] = parseInt(units['heavy_cavalry']);
    units['ram'] = 0;
    units['catapult'] = 0;
    if (total > 400)
        count_per_farming = 1;
    else
        count_per_farming = 0;
    if (count_per_farming != 0)
        setTimeout(get_barb_list, 10,vil,getRotatedTargetXpos(attack_x,attack_y), getRotatedTargetYpos(attack_x,attack_y), units);
}

function getRotatedTargetXpos(xPos, yPos) {
    var dt = new Date();
    return getCircleXPos(getRadian(dt.getHours()), xPos, yPos);
}

function getRotatedTargetYpos(xPos, yPos) {
    var dt = new Date();
    return getCircleYPos(getRadian(dt.getHours()), xPos, yPos);
}

function getRadian(time) {
    return time * 15 *  (Math.PI/180);
}

function getCircleXPos(radian, cx, cy) {
    var tx = parseInt(cx + 5 * Math.cos(radian));
    return tx;
}

function getCircleYPos(radian, cx, cy) {
    var ty = parseInt(cy + 5 * Math.sin(radian));
    return ty;
}

make_village_list()

//go_farm(12733,593,464,{'spear':0,'sword':0,'archer':400,'axe':400,'light_cavalry':200,'mounted_archer':200,'ram':0,'catapult':0,'heavy_cavalry':100})

