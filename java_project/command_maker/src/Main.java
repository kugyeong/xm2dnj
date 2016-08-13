import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
// noble recruiting
// hall of honor units
// farming

public class Main {
	public HashMap<String, Integer[]> recruitCost = new HashMap<String, Integer[]>();
	public HashMap<String, Integer> map = new HashMap<String, Integer>();
	public String[] recruitAvailable;
	Integer[] required_res;
	Integer[] minting_res = new Integer[] { 28000, 30000, 25000 };
	ArrayList<VilInfo> globalVilInfo = new ArrayList<VilInfo>();
	String global_file_name;
	int reloc_amount = 0;
	public HashMap<String, Integer> recruitNum = new HashMap<String, Integer>();
	String vilInfoFile;
	class VilInfo {
		public int vilId;
		public int req_wood;
		public int req_clay;
		public int req_iron;
		public int remain_food;
		public int has_academy;
		public int x;
		public int y;
		public int recruit;
		public int distance;
		public int loyalty;
	}

	public Main() {
		recruitCost.put("spear", new Integer[] { 50, 30, 20, 1 });
		recruitCost.put("sword", new Integer[] { 30, 30, 70, 1 });
		recruitCost.put("archer", new Integer[] { 80, 30, 60, 1 });
		recruitCost.put("heavy_cavalry", new Integer[] { 200, 150, 600, 6 });
		recruitCost.put("axe", new Integer[] { 60, 30, 40, 1 });
		recruitCost.put("light_cavalry", new Integer[] { 125, 100, 250, 4 });
		recruitCost.put("mounted_archer", new Integer[] { 250, 200, 100, 5 });
		recruitCost.put("ram", new Integer[] { 300, 200, 200, 5 });
		recruitCost.put("catapult", new Integer[] { 320, 400, 100, 8 });
		recruitCost.put("trebuchet", new Integer[] { 4000, 2000, 2000, 10 });
		recruitCost.put("doppelsoldner", new Integer[] { 1200, 1200, 2400, 10 });
		
		recruitNum.put("spear", 20);
		recruitNum.put("sword", 20);
		recruitNum.put("archer", 20);
		recruitNum.put("heavy_cavalry", 5);
		recruitNum.put("axe", 20);
		recruitNum.put("light_cavalry", 10);
		recruitNum.put("mounted_archer", 10);
		recruitNum.put("ram", 2);
		recruitNum.put("catapult", 0);
		recruitNum.put("trebuchet", 6);
		recruitNum.put("doppelsoldner", 6);
	}
	
	public void readRecruitNumInfo(String fileName) {
		String s = new String();
		String key;
		String value;
		try {
			// //////////////////////////////////////////////////////////////
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			while ((s = in.readLine()) != null) {
				System.out.println(s);
				key = s.split(":")[0];
				value = s.split(":")[1];
				recruitNum.put(key,Integer.parseInt(value));
			}
			in.close();
			// //////////////////////////////////////////////////////////////
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
			System.exit(1);
		}
	}
	
	public void readGlobalInfo(String fileName) {
		String s = new String();
		global_file_name = fileName;
		String key;
		String value;
		try {
			// //////////////////////////////////////////////////////////////
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			while ((s = in.readLine()) != null) {
				System.out.println(s);
				VilInfo temp = new VilInfo();
				String[] info_list = s.split(",");
				for (String info : info_list) {
					if (info.split(":").length != 2)
						continue;
					key = info.split(":")[0];
					value = info.split(":")[1];
					if (key.equals("vil_id")) {
						temp.vilId = Integer.parseInt(value);
					} else if (key.equals("req_wood")) {
						temp.req_wood = Integer.parseInt(value);
					} else if (key.equals("req_clay")) {
						temp.req_clay = Integer.parseInt(value);
					} else if (key.equals("req_iron")) {
						temp.req_iron = Integer.parseInt(value);
					} else if (key.equals("remain_food")) {
						temp.remain_food = Integer.parseInt(value);
					} else if (key.equals("has_academy")) {
						temp.has_academy = Integer.parseInt(value);
					} else if (key.equals("x")) {
						temp.x = Integer.parseInt(value);
					} else if (key.equals("y")) {
						temp.y = Integer.parseInt(value);
					} else if (key.equals("loyalty")) {
						temp.loyalty = Integer.parseInt(value);
					} else if (key.equals("recruit")) {
						temp.recruit = Integer.parseInt(value);
					}
				}
				temp.distance = Math.abs(map.get("x") - temp.x) + Math.abs(map.get("y") - temp.y);
				globalVilInfo.add(temp);

			}
			in.close();
			// //////////////////////////////////////////////////////////////
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
			System.exit(1);
		}
		VilInfo targetVil = null;
		for (VilInfo vilInfo : globalVilInfo) {
			if (vilInfo.vilId == map.get("id")) {
				targetVil = vilInfo;
				break;
			}
		}
		if(targetVil == null) {
			initGlobalInfo();
		}
	}
	public void initGlobalInfo() {
		VilInfo targetVil = new VilInfo();
		targetVil.vilId = map.get("id");
		
		targetVil.req_wood = 0;
		
		targetVil.req_clay = 0;
		
		targetVil.req_iron = 0;
		
		targetVil.remain_food = map.get("res_food");
		
		targetVil.has_academy = map.get("academy_lvl");
		
		targetVil.x = map.get("x");
		
		targetVil.y = map.get("y");
		targetVil.recruit = 1;
		targetVil.loyalty = map.get("loyalty");
		globalVilInfo.add(targetVil);
		
	}
	public int findVilHasFood() {
		int vilId = -1;
		for (VilInfo vilInfo : globalVilInfo) {
			if (vilInfo.loyalty == 100 && vilInfo.remain_food > required_res[3]) {
				vilId = vilInfo.vilId;
			}
		}
		return vilId;
	}

	public void readVilInfo(String fileName) {
		String s = new String();
		String key;
		String value;
		try {
			// //////////////////////////////////////////////////////////////
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			s = in.readLine();
			in.close();
			// //////////////////////////////////////////////////////////////
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
			System.exit(1);
		}
		String[] info_list = s.split(",");
		for (String info : info_list) {
			if (info.split(":").length != 2)
				continue;
			key = info.split(":")[0];
			value = info.split(":")[1];
			if (key.equals("preceptory_order")) {
				if (value.equals("teutonic"))
					map.put(key, 2);
				else if (value.equals("templar"))
					map.put(key, 1);
				else
					map.put(key, 0);
				continue;
			}
			map.put(key, Integer.parseInt(value));
		}
		int barrack_lvl = map.get("barracks_lvl");
		if (barrack_lvl < 3)
			recruitAvailable = new String[] { "spear" };
		else if (barrack_lvl < 5)
			recruitAvailable = new String[] { "spear", "sword" };
		else if (barrack_lvl < 9)
			recruitAvailable = new String[] { "spear", "sword", "axe" };
		else if (barrack_lvl < 11)
			recruitAvailable = new String[] { "spear", "sword", "axe", "archer" };
		else if (barrack_lvl < 13)
			recruitAvailable = new String[] { "spear", "sword", "axe",
					"archer", "light_cavalry" };
		else if (barrack_lvl < 15)
			recruitAvailable = new String[] { "spear", "sword", "axe",
					"archer", "light_cavalry", "mounted_archer" };
		else if (barrack_lvl < 17)
			recruitAvailable = new String[] { "spear", "sword", "axe",
					"archer", "light_cavalry", "mounted_archer", "ram" };
		else if (barrack_lvl < 21)
			recruitAvailable = new String[] { "spear", "sword", "axe",
					"archer", "light_cavalry", "mounted_archer", "ram",
					"catapult" };
		else
			recruitAvailable = new String[] { "spear", "sword", "axe",
					"archer", "light_cavalry", "mounted_archer", "ram",
					"catapult", "heavy_cavalry" };
		

		
		// calculate resource
		required_res = new Integer[] { 0, 0, 0, 0 };
		for (int i = 0; i < recruitAvailable.length; i++) {
			required_res[0] += recruitCost.get(recruitAvailable[i])[0] * recruitNum.get(recruitAvailable[i]);
			required_res[1] += recruitCost.get(recruitAvailable[i])[1] * recruitNum.get(recruitAvailable[i]);
			required_res[2] += recruitCost.get(recruitAvailable[i])[2] * recruitNum.get(recruitAvailable[i]);
			required_res[3] += recruitCost.get(recruitAvailable[i])[3] * recruitNum.get(recruitAvailable[i]);
		}
		if (map.get("preceptory_order") == 1) {
			required_res[0] += recruitCost.get("doppelsoldner")[0]*recruitNum.get("doppelsoldner");
			required_res[1] += recruitCost.get("doppelsoldner")[1]*recruitNum.get("doppelsoldner");
			required_res[2] += recruitCost.get("doppelsoldner")[2]*recruitNum.get("doppelsoldner");
			required_res[3] += recruitCost.get("doppelsoldner")[3]*recruitNum.get("doppelsoldner");
		} else if(map.get("preceptory_order") == 2) {
			required_res[0] += recruitCost.get("trebuchet")[0]*recruitNum.get("trebuchet");
			required_res[1] += recruitCost.get("trebuchet")[1]*recruitNum.get("trebuchet");
			required_res[2] += recruitCost.get("trebuchet")[2]*recruitNum.get("trebuchet");
			required_res[3] += recruitCost.get("trebuchet")[3]*recruitNum.get("trebuchet");
		}
		required_res[3] += 150;
		
		vilInfoFile = fileName;
	}

	public void writeGlobalInfo(int vil_id, String key, String value) {
		VilInfo targetVil = null;
		
		for (VilInfo vilInfo : globalVilInfo) {
			if (vilInfo.vilId == vil_id) {
				targetVil = vilInfo;
				break;
			}
		}
		if (targetVil != null) {
			if (key.equals("req_wood")) {
				targetVil.req_wood = Integer.parseInt(value);
			} else if (key.equals("req_clay")) {
				targetVil.req_clay = Integer.parseInt(value);
			} else if (key.equals("req_iron")) {
				targetVil.req_iron = Integer.parseInt(value);
			} else if (key.equals("remain_food")) {
				targetVil.remain_food = Integer.parseInt(value);
			} else if (key.equals("has_academy")) {
				targetVil.has_academy = Integer.parseInt(value);
			} else if (key.equals("recruit")) {
				targetVil.recruit = Integer.parseInt(value);
			}
		}
	}
	public int getGlobalInfo(int vil_id, String key) {
		VilInfo targetVil = null;
		int value = -1;
		for (VilInfo vilInfo : globalVilInfo) {
			if (vilInfo.vilId == vil_id) {
				targetVil = vilInfo;
				break;
			}
		}
		if (targetVil != null) {
			if (key.equals("req_wood")) {
				value = targetVil.req_wood;
			} else if (key.equals("req_clay")) {
				value = targetVil.req_clay;
			} else if (key.equals("req_iron")) {
				value = targetVil.req_iron;
			} else if (key.equals("remain_food")) {
				value = targetVil.remain_food;
			} else if (key.equals("has_academy")) {
				value = targetVil.has_academy;
			} else if (key.equals("recruit")) {
				value = targetVil.recruit;
			}
		}
		return value;
	}

	public String make_reloc_troops(int total) {
		int sp_num = 0;
		int sw_num = 0;
		int axe_num = 0;
		int ar_num = 0;
		int lc_num = 0;
		int ma_num = 0;
		int ram_num = 0;
		int cat_num = 0;
		int hc_num = 0;
		int sum = 0;
		String reloc_command = "";
		while (total > sum) {
			int temp_sum = sum;
			if (map.get("heavy_cavalry") > 10) {
				hc_num += 10;
				map.put("heavy_cavalry", map.get("heavy_cavalry") - 10);
				sum += 10 * recruitCost.get("heavy_cavalry")[3];
				if (sum > total)
					break;
			}
			if (map.get("mounted_archer") > 10) {
				ma_num += 10;
				map.put("mounted_archer", map.get("mounted_archer") - 10);
				sum += 10 * recruitCost.get("mounted_archer")[3];
				if (sum > total)
					break;
			}
			if (map.get("light_cavalry") > 10) {
				lc_num += 10;
				map.put("light_cavalry", map.get("light_cavalry") - 10);
				sum += 10 * recruitCost.get("light_cavalry")[3];
				if (sum > total)
					break;
			}
			if (map.get("archer") > 20) {
				ar_num += 20;
				map.put("archer", map.get("archer") - 20);
				sum += 20 * recruitCost.get("archer")[3];
				if (sum > total)
					break;
			}
			if (map.get("axe") > 20) {
				axe_num += 20;
				map.put("axe", map.get("axe") - 20);
				sum += 20 * recruitCost.get("axe")[3];
				if (sum > total)
					break;
			}
			if (map.get("sword") > 20) {
				sw_num += 20;
				map.put("sword", map.get("sword") - 20);
				sum += 20 * recruitCost.get("sword")[3];
				if (sum > total)
					break;
			}
			if (map.get("spear") > 20) {
				sp_num += 20;
				map.put("spear", map.get("spear") - 20);
				sum += 20 * recruitCost.get("spear")[3];
				if (sum > total)
					break;
			}
			if(temp_sum == sum) {
				break;
			}
		}
		reloc_command += "{'spear':" + sp_num + ",'sword':" + sw_num
				+ ",'archer':" + ar_num + ",'axe':" + axe_num
				+ ",'light_cavalry':" + lc_num + ",'mounted_archer':" + ma_num
				+ ",'ram':" + ram_num + ",'catapult':" + cat_num
				+ ",'heavy_cavalry':" + hc_num + "}";
		reloc_amount = sum;
		return reloc_command;
	}
	public String makeRecruitCommands() {
		String commands = "";
		if ( recruitAvailable.length >= 1 ) {
			if(recruitNum.get(recruitAvailable[0]) != 0)
				commands += "barrack_recruit("+map.get("id")+", \"spear\","+recruitNum.get(recruitAvailable[0])+");";
		}
		if ( recruitAvailable.length >= 2 ) {
			if(recruitNum.get(recruitAvailable[1]) != 0)
				commands += "barrack_recruit("+map.get("id")+", \"sword\","+recruitNum.get(recruitAvailable[1])+");";
		}
		if ( recruitAvailable.length >= 3 ) {
			if(recruitNum.get(recruitAvailable[2]) != 0)
				commands += "barrack_recruit("+map.get("id")+", \"axe\","+recruitNum.get(recruitAvailable[2])+");";
		}
		if ( recruitAvailable.length >= 4 ) {
			if(recruitNum.get(recruitAvailable[3]) != 0)
				commands += "barrack_recruit("+map.get("id")+", \"archer\","+recruitNum.get(recruitAvailable[3])+");";
		}
		if ( recruitAvailable.length >= 5 ) {
			if(recruitNum.get(recruitAvailable[4]) != 0)
				commands += "barrack_recruit("+map.get("id")+", \"light_cavalry\","+recruitNum.get(recruitAvailable[4])+");";
		}
		if ( recruitAvailable.length >= 6 ) {
			if(recruitNum.get(recruitAvailable[5]) != 0)
				commands += "barrack_recruit("+map.get("id")+", \"mounted_archer\","+recruitNum.get(recruitAvailable[5])+");";
		}
		if ( recruitAvailable.length >= 7 ) {
			if(recruitNum.get(recruitAvailable[6]) != 0)
				commands += "barrack_recruit("+map.get("id")+", \"ram\","+recruitNum.get(recruitAvailable[6])+");";
		}
		if ( recruitAvailable.length >= 8 ) {
			if(recruitNum.get(recruitAvailable[7]) != 0)
				commands += "barrack_recruit("+map.get("id")+", \"catapult\","+recruitNum.get(recruitAvailable[7])+");";
		}
		if ( recruitAvailable.length >= 9 ) {
			if(recruitNum.get(recruitAvailable[8]) != 0)
				commands += "barrack_recruit("+map.get("id")+", \"heavy_cavalry\","+recruitNum.get(recruitAvailable[8])+");";
		}
		
		return commands;
	}

	public String makePreceptoryRecruitCommands() {
		String commands = "";
		if (map.get("preceptory_order") == 1) {
			if (recruitNum.get("doppelsoldner") != 0)
				commands += "preceptory_recruit(" + map.get("id")
					+ ", \"doppelsoldner\","+recruitNum.get("doppelsoldner")+");";
		} else if (map.get("preceptory_order") == 2) {
			if (recruitNum.get("trebuchet") != 0)
				commands += "preceptory_recruit(" + map.get("id")
					+ ", \"trebuchet\","+recruitNum.get("trebuchet")+");";
		}

		return commands;
	}

	public void calcRes() {
		// calc res
		map.put("res_wood", map.get("res_wood") - required_res[0]);
		map.put("res_clay", map.get("res_clay") - required_res[1]);
		map.put("res_iron", map.get("res_iron") - required_res[2]);
		map.put("res_food", map.get("res_food") - (required_res[3]-150));
	}

	public void setRecruitSucessInfo() {
		// update global req resourse by 0
		writeGlobalInfo(map.get("id"), "req_wood", "0");
		writeGlobalInfo(map.get("id"), "req_clay", "0");
		writeGlobalInfo(map.get("id"), "req_iron", "0");
		writeGlobalInfo(map.get("id"), "recruit", "1");
	}
	public void makeCommand() {
		int recruit_flag = 1;
		

		String commands = new String();
		if (map.get("barrack_queue_len") < 3) {
			if (map.get("res_wood") >= required_res[0]
					&& map.get("res_clay") >= required_res[1]
					&& map.get("res_iron") >= required_res[2]
					&& map.get("res_food") >= required_res[3]) {
				// make recruit commands
				commands += makeRecruitCommands();
				commands += makePreceptoryRecruitCommands();
				calcRes();
				setRecruitSucessInfo();
				
			} else if (map.get("res_wood") >= required_res[0]
					&& map.get("res_clay") >= required_res[1]
					&& map.get("res_iron") >= required_res[2]
					&& map.get("res_food") < required_res[3]) {
				// relocate and make recruit commands
				int target_vil_id = findVilHasFood();
				if (target_vil_id == -1) {
					writeGlobalInfo(map.get("id"), "recruit", "0");
				} else {
					int total = required_res[3] - map.get("res_food");
					String reloc_command = make_reloc_troops(total);
					if(reloc_amount > 0) {
						commands += "send_custom_army("
							+ map.get("id")
							+ ", "
							+ Integer.toString(target_vil_id)
							+ ", "
							+ "'relocate',"+reloc_command+", 'wall', "
							+ "{'bastard':false, 'leader':false, 'loot_master':false, 'medic':false, 'scout':false, 'supporter':false}"
							+ ", 65805);";
						map.put("res_food", map.get("res_food") + reloc_amount);	
						writeGlobalInfo(
								target_vil_id,
								"remain_food",
								Integer.toString(getGlobalInfo(target_vil_id,
										"remain_food") - reloc_amount));
					}
					
					if (reloc_amount >= total) {
						commands += makeRecruitCommands();
						commands += makePreceptoryRecruitCommands();
						calcRes();
						setRecruitSucessInfo();
					} else {
						writeGlobalInfo(map.get("id"), "recruit", "0");
					}
				}
			} else {
				if (getGlobalInfo(map.get("id"), "recruit") == 1) {
					writeGlobalInfo(map.get("id"), "recruit", "0");
					if (map.get("res_wood") < required_res[0]) {
						// write global resource requirement
						// if flag is 1 update
						writeGlobalInfo(map.get("id"), "req_wood",
								Integer.toString(required_res[0] - map.get("res_wood")));

					}
					if (map.get("res_clay") < required_res[1]) {
						// write global resource requirement
						writeGlobalInfo(map.get("id"), "req_clay",
								Integer.toString(required_res[1] - map.get("res_clay")));
					}
					if (map.get("res_iron") < required_res[2]) {
						// write global resource requirement
						writeGlobalInfo(map.get("id"), "req_iron",
								Integer.toString(required_res[2] - map.get("res_iron")));
					}
				}
				if (map.get("res_food") < required_res[3]) {
					// make relocate command
					int target_vil_id = findVilHasFood();
					if (target_vil_id == -1) {
						writeGlobalInfo(map.get("id"), "recruit", "0");
					} else {
						int total = required_res[3] - map.get("res_food");
						String reloc_command = make_reloc_troops(total);
						if(reloc_amount > 0) {
							commands += "send_custom_army("
								+ map.get("id")
								+ ", "
								+ Integer.toString(target_vil_id)
								+ ", "
								+ "'relocate',"+reloc_command+", 'wall', "
								+ "{'bastard':false, 'leader':false, 'loot_master':false, 'medic':false, 'scout':false, 'supporter':false}"
								+ ", 65805);";
							map.put("res_food", map.get("res_food") + reloc_amount);	
							writeGlobalInfo(
									target_vil_id,
									"remain_food",
									Integer.toString(getGlobalInfo(target_vil_id,
											"remain_food") - reloc_amount));
						}
					}
				}
			}
		}
		writeGlobalInfo(map.get("id"), "remain_food", Integer.toString(map.get("res_food")));
		// minting
		writeGlobalInfo(map.get("id"), "has_academy", Integer.toString(map.get("academy_lvl")));
		if (map.get("academy_lvl") > 0) {
			if (map.get("build_queue_len") == 0) {
				// make minting command
				//50000, 75000, 40000
				int req_wood;
				int req_clay;
				int req_iron;
				if (map.get("farm_lvl") < 30) {
					req_wood = 101000;
					req_clay = 125000;
					req_iron = 76000;
				} else {
					req_wood = 50000;
					req_clay = 75000;
					req_iron = 40000;
				}
				int count = 0;
				int temp = 0;
				temp = (map.get("res_wood") - req_wood) / minting_res[0];
				count = temp;
				temp = (map.get("res_clay") - req_clay) / minting_res[1];
				if (count > temp)
					count = temp;
				temp = (map.get("res_iron") - req_iron) / minting_res[2];
				if (count > temp)
					count = temp;
				if (count > 0) {
					commands += "mint_coins("+map.get("id")+","+count+");";
					// calc res
					map.put("res_wood", map.get("res_wood") - count*minting_res[0]);
					map.put("res_clay", map.get("res_clay") - count*minting_res[1]);
					map.put("res_iron", map.get("res_iron") - count*minting_res[2]);
				}
			} else {
				int count = 0;
				int temp = 0;
				temp = (map.get("res_wood") - required_res[0]) / minting_res[0];
				count = temp;
				temp = (map.get("res_clay") - required_res[1]) / minting_res[1];
				if (count > temp)
					count = temp;
				temp = (map.get("res_iron") - required_res[2]) / minting_res[2];
				if (count > temp)
					count = temp;
				if (count != 0) {
					commands += "mint_coins("+map.get("id")+","+count+");";
					// calc res
					map.put("res_wood", map.get("res_wood") - count*minting_res[0]);
					map.put("res_clay", map.get("res_clay") - count*minting_res[1]);
					map.put("res_iron", map.get("res_iron") - count*minting_res[2]);
				}
			}
		}
		// recruit fail flag
		if (getGlobalInfo(map.get("id"), "recruit") == 1 && map.get("free_merchant") > 0) {
			if (map.get("res_wood") > required_res[0] + 1000) {
				// search a village requires resource
				VilInfo targetVil = null;
				int distance = -1;
				for (VilInfo vilInfo : globalVilInfo) {
					if (vilInfo.distance != 0 && vilInfo.req_wood > 0 && distance == -1 && vilInfo.distance < 10) {
						distance = vilInfo.distance;
						targetVil = vilInfo;
						continue;
					}
					if (vilInfo.distance != 0 && vilInfo.req_wood > 0 && distance > vilInfo.distance) {
						distance = vilInfo.distance;
						targetVil = vilInfo;
					}
				}
				if (targetVil != null) {
					int req_wood = (int) Math
							.ceil(targetVil.req_wood / 1000.0) * 1000;
					int send_amount;
					if (req_wood > map.get("free_merchant") * 1000) {
						send_amount = map.get("free_merchant") * 1000;
					} else {
						send_amount = req_wood;
					}
					commands += "trading_send_resources(" + map.get("id")
							+ ", " + targetVil.vilId + ", "
							+ Integer.toString(send_amount) + ", 0, 0);";
					map.put("res_wood", map.get("res_wood") - send_amount);
					map.put("free_merchant", map.get("free_merchant") - send_amount/1000);
					targetVil.req_wood = targetVil.req_wood - send_amount;
					if (targetVil.req_wood <= 0)
						targetVil.req_wood = -1;
				}
			}
			if (map.get("res_clay") > required_res[1] + 1000 && map.get("free_merchant") > 0) {
				// search a village requires resource
				VilInfo targetVil = null;
				int distance = -1;
				for (VilInfo vilInfo : globalVilInfo) {
					if (vilInfo.distance != 0 && vilInfo.req_clay > 0 && distance == -1 && vilInfo.distance < 10) {
						distance = vilInfo.distance;
						targetVil = vilInfo;
						continue;
					}
					if (vilInfo.distance != 0 && vilInfo.req_clay > 0 && distance > vilInfo.distance) {
						distance = vilInfo.distance;
						targetVil = vilInfo;
					}
				}
				if (targetVil != null) {
					int req_clay = (int) Math
							.ceil(targetVil.req_clay / 1000.0) * 1000;
					int send_amount;
					if (req_clay > map.get("free_merchant") * 1000) {
						send_amount = map.get("free_merchant") * 1000;
					} else {
						send_amount = req_clay;
					}
					commands += "trading_send_resources(" + map.get("id")
							+ ", " + targetVil.vilId + ", " + "0,"
							+ Integer.toString(send_amount) + ", 0);";
					map.put("res_clay", map.get("res_clay") - send_amount);
					map.put("free_merchant", map.get("free_merchant") - send_amount/1000);
					targetVil.req_clay = targetVil.req_clay - send_amount;
					if (targetVil.req_clay <= 0)
						targetVil.req_clay = -1;
				}
			}
			if (map.get("res_iron") > required_res[2] + 1000 && map.get("free_merchant") > 0) {
				// search a village requires resource
				VilInfo targetVil = null;
				int distance = -1;
				for (VilInfo vilInfo : globalVilInfo) {
					if (vilInfo.distance != 0 && vilInfo.req_iron > 0 && distance == -1 && vilInfo.distance < 10) {
						distance = vilInfo.distance;
						targetVil = vilInfo;
						continue;
					}
					if (vilInfo.distance != 0 && vilInfo.req_iron > 0 && distance > vilInfo.distance) {
						distance = vilInfo.distance;
						targetVil = vilInfo;
					}
				}
				if (targetVil != null) {
					int req_iron = (int) Math
							.ceil(targetVil.req_iron / 1000.0) * 1000;
					int send_amount;
					if (req_iron > map.get("free_merchant") * 1000) {
						send_amount = map.get("free_merchant") * 1000;
					} else {
						send_amount = req_iron;
					}
					commands += "trading_send_resources(" + map.get("id")
							+ ", " + targetVil.vilId + ", " + "0, 0, "
							+ Integer.toString(send_amount) + ");";
					map.put("res_iron", map.get("res_iron") - send_amount);
					map.put("free_merchant", map.get("free_merchant") - send_amount/1000);
					targetVil.req_iron = targetVil.req_iron - send_amount;
					if (targetVil.req_iron <= 0)
						targetVil.req_iron = -1;
				}
			}
			// recruit fail flag
			// building management
			if (map.get("build_queue_len") == 0) {
				if (map.get("headquarter_lvl") < 8) {
					if ( map.get("res_wood") >= 3800
							&& map.get("res_clay") >= 4000
							&& map.get("res_iron") >= 3600
							&& map.get("res_food") >= 2) {
						commands += "vil_build(" + map.get("id")
								+ ", \"headquarter\");";
					}
				} else if (map.get("wall_lvl") < 15) {
						if( map.get("res_wood") >= 1300
						&& map.get("res_clay") >= 3000
						&& map.get("res_iron") >= 510
						&& map.get("res_food") >= 7) {
					// make building wall command
					commands += "vil_build(" + map.get("id") + ", \"wall\");";
						}
				} else if (map.get("warehouse_lvl") < 25){
						if(map.get("res_wood") >= 19000
						&& map.get("res_clay") >= 18000
						&& map.get("res_iron") >= 16000) {
					commands += "vil_build(" + map.get("id")
							+ ", \"warehouse\");";
						}
				} else if (map.get("market_lvl") < 10){
					if( map.get("res_wood") >= 800
						&& map.get("res_clay") >= 890
						&& map.get("res_iron") >= 800
						&& map.get("res_food") >= 12) {
					commands += "vil_build(" + map.get("id") + ", \"market\");";
					}
				} else if (map.get("farm_lvl") < 26){
						if(map.get("res_wood") >= 34000
						&& map.get("res_clay") >= 43000
						&& map.get("res_iron") >= 26000) {
					commands += "vil_build(" + map.get("id") + ", \"farm\");";
						}
				} else if (map.get("warehouse_lvl") < 28){
						if(map.get("res_wood") >= 34000
						&& map.get("res_clay") >= 33000
						&& map.get("res_iron") >= 29000) {
					commands += "vil_build(" + map.get("id")
							+ ", \"warehouse\");";
						}
				} else if (map.get("farm_lvl") < 30){
					if(map.get("res_wood") >= 101000
						&& map.get("res_clay") >= 126000
						&& map.get("res_iron") >= 76000) {
					commands += "vil_build(" + map.get("id") + ", \"farm\");";
					}
				} else if (map.get("barracks_lvl") < 21){
					if(map.get("res_wood") >= 26000
						&& map.get("res_clay") >= 37000
						&& map.get("res_iron") >= 30000
						&& map.get("res_food") >= 44) {
					commands += "vil_build(" + map.get("id")
							+ ", \"barracks\");";
					}
				} else if (map.get("headquarter_lvl") < 20){
					if(map.get("res_wood") >= 40000
						&& map.get("res_clay") >= 50000
						&& map.get("res_iron") >= 30000
						&& map.get("res_food") >= 15) {
					commands += "vil_build(" + map.get("id")
							+ ", \"headquarter\");";
					}
				} else if (map.get("academy_lvl") < 1 ) {
					if(map.get("res_wood") >= 60000
						&& map.get("res_clay") >= 75000
						&& map.get("res_iron") >= 60000
						&& map.get("res_food") >= 80) {
					commands += "vil_build(" + map.get("id")
							+ ", \"academy\");";
					}
				} else if (map.get("rally_point_lvl") < 5) {
					if( map.get("res_wood") >= 13000
						&& map.get("res_clay") >= 18000
						&& map.get("res_iron") >= 25000
						&& map.get("res_food") >= 16) {
					commands += "vil_build(" + map.get("id")
							+ ", \"rally_point\");";
					}
				} else if (map.get("statue_lvl") < 5) {
					if(map.get("res_wood") >= 10000
						&& map.get("res_clay") >= 10000
						&& map.get("res_iron") >= 20000) {
					commands += "vil_build(" + map.get("id") + ", \"statue\");";
					}
				} else if (map.get("hospital_lvl") < 10) {
						if(map.get("res_wood") >= 32000
						&& map.get("res_clay") >= 48000
						&& map.get("res_iron") >= 19200
						&& map.get("res_food") >= 17) {
					commands += "vil_build(" + map.get("id")
							+ ", \"hospital\");";
						}
				} else if (map.get("barracks_lvl") < 25) {
					if(map.get("res_wood") >= 30000
						&& map.get("res_clay") >= 55000
						&& map.get("res_iron") >= 38000
						&& map.get("res_food") >= 92) {
					commands += "vil_build(" + map.get("id")
							+ ", \"barracks\");";
					}
				} else if (map.get("tavern_lvl") < 14) {
					if(map.get("res_wood") >= 5900
						&& map.get("res_clay") >= 4900
						&& map.get("res_iron") >= 3500
						&& map.get("res_food") >= 60) {
					commands += "vil_build(" + map.get("id") + ", \"tavern\");";
					}
				} else if (map.get("headquarter_lvl") < 25) {
					if(map.get("res_wood") >= 50000
						&& map.get("res_clay") >= 75000
						&& map.get("res_iron") >= 40000
						&& map.get("res_food") >= 31) {
					commands += "vil_build(" + map.get("id")
							+ ", \"headquarter\");";
					}
				} else if (map.get("preceptory_lvl") < 1) {
					if(map.get("res_wood") >= 35000
						&& map.get("res_clay") >= 50000
						&& map.get("res_iron") >= 30000
						&& map.get("res_food") >= 11) {
					commands += "vil_build(" + map.get("id")
							+ ", \"preceptory\");";
					}
				} else if (map.get("wall_lvl") < 20) {
					if(map.get("res_wood") >= 4000
							&& map.get("res_clay") >= 10100
							&& map.get("res_iron") >= 1600
							&& map.get("res_food") >= 15) {
						commands += "vil_build(" + map.get("id")
								+ ", \"wall\");";
						}
				}  else if (map.get("warehouse_lvl") < 30) {
					if(map.get("res_wood") >= 50000
							&& map.get("res_clay") >= 48000
							&& map.get("res_iron") >= 43000) {
						commands += "vil_build(" + map.get("id")
								+ ", \"warehouse\");";
						}
				} else if (map.get("timber_camp_lvl") < 30) {
					if(map.get("res_wood") >= 24000
						&& map.get("res_clay") >= 30000
						&& map.get("res_iron") >= 18000
						&& map.get("res_food") >= 43) {
					commands += "vil_build(" + map.get("id")
							+ ", \"timber_camp\");";
					}
				} else if (map.get("clay_pit_lvl") < 30) {
					if(map.get("res_wood") >= 30000
						&& map.get("res_clay") >= 24000
						&& map.get("res_iron") >= 18000
						&& map.get("res_food") >= 55) {
					commands += "vil_build(" + map.get("id")
							+ ", \"clay_pit\");";
					}
				} else if (map.get("iron_mine_lvl") < 30) {
					if(map.get("res_wood") >= 36000
						&& map.get("res_clay") >= 30000
						&& map.get("res_iron") >= 36000
						&& map.get("res_food") >= 138) {
					commands += "vil_build(" + map.get("id")
							+ ", \"iron_mine\");";
					}
				}  else if (map.get("market_lvl") < 25){
					if( map.get("res_wood") >= 25600
							&& map.get("res_clay") >= 34100
							&& map.get("res_iron") >= 25600
							&& map.get("res_food") >= 126) {
						commands += "vil_build(" + map.get("id") + ", \"market\");";
					}
				}
				/*else if (map.get("market_lvl") < 25 && map.get("res_wood") >= 25600
						&& map.get("res_clay") >= 34100
						&& map.get("res_iron") >= 25600
						&& map.get("res_food") >= 126) {
					commands += "vil_build(" + map.get("id") + ", \"market\");";
				}*/
			}
			if (map.get("academy_lvl") != 1 && map.get("build_queue_len") != 0) {
				if (map.get("free_merchant") >= 3) {
					if (map.get("res_wood") > required_res[0]
							&& map.get("res_wood") > map.get("base_storage") * 60 / 100) {
						// search a village requires resource
						// send res to nearest academy and calculate
						VilInfo targetVil = null;
						int distance = -1;
						for (VilInfo vilInfo : globalVilInfo) {
							if (vilInfo.has_academy == 1 && distance == -1) {
								distance = vilInfo.distance;
								targetVil = vilInfo;
								continue;
							}
							if (vilInfo.has_academy == 1) {
								if (vilInfo.distance != 0
										&& distance > vilInfo.distance) {
									distance = vilInfo.distance;
									targetVil = vilInfo;
								}
							}
						}
						if (targetVil != null) {
							int send_amount = (int) Math.round(map
									.get("free_merchant") / 3) * 1000;

							commands += "trading_send_resources("
									+ map.get("id") + ", " + targetVil.vilId
									+ ", " + Integer.toString(send_amount)
									+ ", 0, 0);";
							map.put("res_wood", map.get("res_wood")
									- send_amount);
						}
					}
					if (map.get("res_clay") > required_res[1]
							&& map.get("res_clay") > map.get("base_storage") * 60 / 100) {
						// search a village requires resource
						VilInfo targetVil = null;
						int distance = -1;
						for (VilInfo vilInfo : globalVilInfo) {
							if (vilInfo.has_academy == 1 && distance == -1) {
								distance = vilInfo.distance;
								targetVil = vilInfo;
								continue;
							}
							if (vilInfo.has_academy == 1) {
								if (vilInfo.distance != 0
										&& distance > vilInfo.distance) {
									distance = vilInfo.distance;
									targetVil = vilInfo;
								}
							}
						}
						if (targetVil != null) {
							int send_amount = (int) Math.round(map
									.get("free_merchant") / 3) * 1000;

							commands += "trading_send_resources("
									+ map.get("id") + ", " + targetVil.vilId
									+ ", " + "0,"
									+ Integer.toString(send_amount) + ", 0);";
							map.put("res_clay", map.get("res_clay")
									- send_amount);
						}
					}
					if (map.get("res_iron") > required_res[2]
							&& map.get("res_iron") > map.get("base_storage") * 60 / 100) {
						// search a village requires resource
						VilInfo targetVil = null;
						int distance = -1;
						for (VilInfo vilInfo : globalVilInfo) {
							if (vilInfo.has_academy == 1 && distance == -1) {
								distance = vilInfo.distance;
								targetVil = vilInfo;
								continue;
							}
							if (vilInfo.has_academy == 1) {
								if (vilInfo.distance != 0
										&& distance > vilInfo.distance) {
									distance = vilInfo.distance;
									targetVil = vilInfo;
								}
							}
						}
						if (targetVil != null) {
							int send_amount = (int) Math.round(map
									.get("free_merchant") / 3) * 1000;

							commands += "trading_send_resources("
									+ map.get("id") + ", " + targetVil.vilId
									+ ", " + "0, 0, "
									+ Integer.toString(send_amount) + ");";
							map.put("res_iron", map.get("res_iron")
									- send_amount);
						}
					}
				}
			}
		}
		commands += makeFarmingCommands();
		 
		writeCommandFile(map.get("id"), commands);
		
	}
	
	public String makeFarmingCommands() {
		String commands = "";
		//go_farm2(12733,593,464,
		//{'spear':11000,'sword':0,'archer':400,'axe':400,'light_cavalry':200,
		//'mounted_archer':200,'ram':0,'catapult':0,'heavy_cavalry':100})
		commands += "go_farm("+map.get("id")+","+map.get("x")+","+map.get("y")+",{'spear':"+map.get("spear")
				+",'sword':"+map.get("sword")+",'archer':"+map.get("archer")+",'axe':"+map.get("axe")
				+",'light_cavalry':"+map.get("light_cavalry")+",'mounted_archer':"+map.get("mounted_archer")
				+",'ram':"+map.get("ram")+",'catapult':"+map.get("catapult")+",'heavy_cavalry':"+map.get("heavy_cavalry")+"});";
		return commands;
	}
	
	public void writeCommandFile(int id, String commands) {
		String fileName = vilInfoFile.substring(0, vilInfoFile.lastIndexOf('.'));
		fileName += "-command.txt";
		try {
		      ////////////////////////////////////////////////////////////////
		      BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		      String[] command_list = commands.split(";");
				//vil_id:45292,req_wood:1000,req_clay:1000,req_iron:1000,remain_food:1000,has_academy:1,x:200,y:200
				for (String command : command_list) {
					out.write(command); out.newLine();
				}
		      out.close();
		      ////////////////////////////////////////////////////////////////
		    } catch (IOException e) {
		        System.err.println(e); // 에러가 있다면 메시지 출력
		        System.exit(1);
		    }
	}
	
	public void flushGlobalInfo() {
		//write global info
		try {
		      ////////////////////////////////////////////////////////////////
		      BufferedWriter out = new BufferedWriter(new FileWriter(global_file_name));
		      VilInfo targetVil = null;
				int distance = 0;
				//vil_id:45292,req_wood:1000,req_clay:1000,req_iron:1000,remain_food:1000,has_academy:1,x:200,y:200
				for (VilInfo vilInfo : globalVilInfo) {
					String s = "vil_id:" + vilInfo.vilId;
					s += ",req_wood:"+vilInfo.req_wood;
					s += ",req_clay:"+vilInfo.req_clay;
					s += ",req_iron:"+vilInfo.req_iron;
					s += ",remain_food:"+vilInfo.remain_food;
					s += ",has_academy:"+vilInfo.has_academy;
					s += ",x:"+vilInfo.x;
					s += ",y:"+vilInfo.y;
					s += ",loyalty:"+vilInfo.loyalty;
					s += ",recruit:"+vilInfo.recruit;
					out.write(s); out.newLine();
				}
		      out.close();
		      ////////////////////////////////////////////////////////////////
		    } catch (IOException e) {
		        System.err.println(e); // 에러가 있다면 메시지 출력
		        System.exit(1);
		    }

	}
	public static void main(String args[]) {

		if (args.length == 0) { // args.length 는 옵션 개수
			System.err.println("Input Filename...");
			System.exit(1); // 읽을 파일명을 주지 않았을 때는 종료
		}
		Main main = new Main();
		main.readRecruitNumInfo(args[2]);
		main.readVilInfo(args[0]);
		main.readGlobalInfo(args[1]);
		main.makeCommand();
		main.flushGlobalInfo();
		/*
		 * for( Map.Entry<String, String> elem : main.map.entrySet() ){
		 * System.out.println( String.format("키 : %s, 값 : %s", elem.getKey(),
		 * elem.getValue()) ); }
		 */
	}
}

