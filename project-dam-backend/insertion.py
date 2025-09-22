# generate_and_upsert.py
# Generates synthetic network-management demo data and upserts directly into MongoDB (Cosmos DB - Mongo API).
# Password hashing matches bcrypt with saltRounds = 10 (same behavior as your TS/Mongoose pre-save).
# Requires: pymongo, bcrypt, dnspython (if using srv), python-dateutil optional
# pip install pymongo bcrypt

import random, math
from ipaddress import IPv4Address, IPv4Network
from datetime import datetime
from pymongo import MongoClient
from bson import ObjectId
import bcrypt

random.seed(42)

def new_objid():
    return ObjectId()

# --- Configuration ---
MONGO_CONN = "mongodb+srv://rgCras:imagensbase64!@dam20251.global.mongocluster.cosmos.azure.com/dam?tls=true&authMechanism=SCRAM-SHA-256&retrywrites=false&maxIdleTimeMS=120000"
DB_NAME = "dam"
COL_USERS = "users"
COL_SITES = "sites"
COL_DEVICES = "devices"
COL_CABLES = "cables"
COL_IPS = "ips"

# Counts requested
num_sites = 72
users_counts = {'guest': 9, 'technician': 4, 'admin': 7}

vendors = ['Cisco', 'Juniper', 'Arista', 'Dell', 'HP','Nokia','Ubiquiti', 'MikroTik', 'Huawei', "Extreme Networks", "Aruba", "Fortinet", "Palo Alto", "Netgear", "TP-Link", "Lenovo", "Supermicro", "F5"]
categories = ['router', 'switch', 'terminal server', 'AP', 'WLC', 'rack', 'server', 'firewall', "IoT gateway", "load balancer", "storage array", "UPS", "modem", "VoIP phone", "camera", "NAS", "SAN switch", "proxy server", "VPN concentrator", "content filter", "network tap", "SD-WAN appliance"]
device_states = ['racked and operational', 'racked and on stand by', 'racked and shut down', 'onboarding', 'inventory', 'disposed', 'maintenance']
cable_types = ['copper', 'fiber', 'serial', 'stacking']
link_speeds = ['100Mbps','1Gbps','2.5Gbps','10Gbps','40Gbps','100Gbps','200Gbps','400Gbps','800Gbps','1.5Tbps']
duplex_opts = ['full','half','auto']

names_pool = ["Alex Mercer","Sam Carter","Robin Blake","Taylor Quinn","Jordan Lee","Morgan Price","Jamie Cole","Casey Rivers","Drew Wood","Cameron Wells","Riley Hart","Avery Stone","Parker Flynn","Quinn Archer","Reese Baylor","Logan Shaw","Rowan Pierce","Kendall Blake","Skyler Mason","Emerson Voss","Blair Keane","Sage Monroe","Dakota Vale","Hayden Frost","Alden Cross","Finley Scott","Gale Foster","Kai Mercer","Lennon Gray","Micah Reed","Nico Drake","Onyx Hale","Penny Rhodes","Rory Sloan","Tatum Miles","Vale Rowan","Wren Archer","Zane Wilder","Ira Quinn","Jules Rivers","Kris Lane","Luca Ford","Marlowe Dean","Noel Ashton","Peyton Clay","River Stone","Shay Ellis","Terry Quinn","Urban Hale","Vega North","Wynn Hart","Yarden Colt","Zephyr Hale"]

countries = ["Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"]


# helper email generator
def gen_email(name, idx):
    base = ''.join(ch for ch in name.lower() if ch.isalnum())
    return f"{base}{idx}@example.edu"

# current timestamp generator (mimic mongoose timestamps)
def now_ts():
    return datetime.utcnow()

# 1) Users
users = []

print("Starting data generation...")
print("Generating users...")
raw_password = "string"
hashed = bcrypt.hashpw(raw_password.encode('utf-8'), bcrypt.gensalt(rounds=10)).decode('utf-8')
iterCounter = 0
uid = 1
for role, count in users_counts.items():
    for i in range(count):
        name = names_pool[(uid-1) % len(names_pool)] + (f" {math.floor(uid/100)}" if uid > len(names_pool) else "")
        user_doc = {
            "_id": new_objid(),
            "name": name,
            "email": gen_email(name, uid).lower(),
            "password": hashed,
            "role": role,
            "isActive": True,
            "createdAt": now_ts(),
            "updatedAt": now_ts()
        }
        users.append(user_doc)
        uid += 1
        if iterCounter % 10000 == 0:
            print(f"  Created {iterCounter} users so far...")
        iterCounter += 1

print(f"Generated {len(users)} users.")
print("Generating sites, devices, cables, and IPs...")
print("Generating sites...")	

uid = 1
iterCounter = 0
# 2) Sites
sites = []
site_ids = [new_objid() for _ in range(num_sites)]
for i in range(num_sites):
    sid = site_ids[i]
    city = f"City{i+1}"
    state = f"State{(i%10)+1}"
    street = f"{100+i} Example St"
    lat = 37.0 + (i * 0.1)
    lon = -122.0 - (i * 0.08)
    site = {
        "_id": sid,
        "localName": f"Site-{i+1:02d}-{city}",
        "type": random.choice(["campus", "datacenter", "branch", "warehouse"]),
        "country": countries[(uid-1) % len(countries)] + (f" {math.floor(uid/100)}" if uid > len(countries) else ""),
        "address": {
            "street": street,
            "city": city,
            "state": state,
            "zipCode": f"{90000 + i}",
            "latitude": round(lat,6),
            "longitude": round(lon,6)
        },
        "devicesAtSite": [],
        "isActive": bool(random.choice([True, True, True, True, True, True, True, True, True, False])),
        "createdAt": now_ts(),
        "updatedAt": now_ts()
    }
    sites.append(site)
    if iterCounter % 10000 == 0:
        print(f"  Created {iterCounter} sites so far...")
    iterCounter += 1
    uid += 1

print(f"Generated {len(sites)} sites.")
print("Generating devices...")

iterCounter = 0
# 3) Devices - between 30 and 45 per site
devices = []
for site in sites:
    ndev = random.randint(2, 70)
    for _ in range(ndev):
        did = new_objid()
        vendor = random.choice(vendors)
        category = random.choice(categories)
        dtype = f"{vendor} {category} model {random.randint(100,999)}"
        mac = ":".join(f"{random.randrange(0,256):02x}" for _ in range(6))
        serial = f"{vendor[:2].upper()}-{random.randint(100000,999999)}-{random.choice('ABCDEFGHIJKLMNOPQRSTUVWXYZ')}"
        state = random.choice(device_states)
        device = {
            "_id": did,
            "vendor": vendor,
            "category": category,
            "type": dtype,
            "serialNumber": serial,
            "macAddress": mac,
            "state": state,
            "site": site["_id"],
            "connectedDevices": [],
            "isActive": True,
            "createdAt": now_ts(),
            "updatedAt": now_ts()
        }
        devices.append(device)
        if iterCounter % 10000 == 0:
            print(f"  Created {iterCounter} devices so far...")
        iterCounter += 1
        site["devicesAtSite"].append(device["_id"])

# Build index of site -> devices for quick selection
site_to_devices = {s["_id"]: s["devicesAtSite"] for s in sites}

print(f"Generated {len(devices)} devices.")
print("Generating cables")

# 4) Cables - For each device, generate between 2 and 24 connections within the same site.
cables = []
cable_pairs = set()
port_counters = {}

def pick_peer(device_id, site_devices):
    peers = [d for d in site_devices if d != device_id]
    return random.choice(peers) if peers else None

iterCounter = 0
for site in sites:
    site_devs = site["devicesAtSite"]
    if not site_devs:
        continue
    desired_degrees = {d: random.randint(2, min(24, max(2, len(site_devs)-1))) for d in site_devs}
    for dev in site_devs:
        target = desired_degrees[dev]
        while len([1 for pair in cable_pairs if dev in pair]) < target:
            peer = pick_peer(dev, site_devs)
            if not peer:
                break
            pair = frozenset([dev, peer])
            if pair in cable_pairs:
                tries = 0
                found = False
                while tries < 10:
                    peer = pick_peer(dev, site_devs)
                    pair = frozenset([dev, peer])
                    if pair not in cable_pairs:
                        found = True
                        break
                    tries += 1
                if not found:
                    break
            cable_pairs.add(pair)
            for did in (dev, peer):
                port_counters.setdefault(did, 0)
            port_counters[dev] += 1
            port_counters[peer] += 1
            iface1 = {
                "device": dev,
                "portId": f"Gig{random.randint(0,3)}/{port_counters[dev]}",
                "linkSpeed": random.choice(link_speeds),
                "duplex": random.choice(duplex_opts)
            }
            iface2 = {
                "device": peer,
                "portId": f"Gig{random.randint(0,3)}/{port_counters[peer]}",
                "linkSpeed": random.choice(link_speeds),
                "duplex": random.choice(duplex_opts)
            }
            cable = {
                "_id": new_objid(),
                "device1": dev,
                "device2": peer,
                "cableType": random.choice(cable_types),
                "interface1": iface1,
                "interface2": iface2,
                "createdAt": now_ts(),
                "updatedAt": now_ts()
            }
            cables.append(cable)
            if iterCounter % 10000 == 0:
                print(f"  Created {iterCounter} cables so far...")
            iterCounter += 1

# Update each device connectedDevices based on cables
dev_map = {d["_id"]: d for d in devices}
for c in cables:
    d1, d2 = c["device1"], c["device2"]
    if d2 not in dev_map[d1]["connectedDevices"]:
        dev_map[d1]["connectedDevices"].append(d2)
        dev_map[d1]["updatedAt"] = now_ts()
    if d1 not in dev_map[d2]["connectedDevices"]:
        dev_map[d2]["connectedDevices"].append(d1)
        dev_map[d2]["updatedAt"] = now_ts()

print(f"Generated {len(cables)} cables.")
print("Generating IPs")

# 5) IPs - one /31 subnet for each cable connection, and ~10 host IPs per device for services
ips = []
base_net = IPv4Network("10.0.0.0/8")
current_int = int(base_net.network_address)

def next_subnet_ip():
    global current_int
    addr = IPv4Address(current_int)
    current_int += 2
    return str(addr)

# Create /31 per cable
for c in cables:
    ip_addr = next_subnet_ip()
    entry = {
        "_id": new_objid(),
        "ip": ip_addr,
        "mask": 31,
        "type": "subnet",
        "logicalEntity": "point-to-point link",
        "isActive": True,
        "devices": [c["device1"], c["device2"]],
        "createdAt": now_ts(),
        "updatedAt": now_ts()
    }
    ips.append(entry)

iterCounter = 0
# Now create ~10 host IPs per device
for d in devices:
    for j in range(10):
        addr = str(IPv4Address(current_int))
        current_int += 1
        ip_entry = {
            "_id": new_objid(),
            "ip": addr,
            "mask": 32,
            "type": "host",
            "logicalEntity": random.choice(["management interface","service","SNMP agent","BGP peer","OSPF router id","DHCP relay agent","DNS server","NTP server","FHRP Virtual IP","default gateway", "VPN endpoint", "web server", "email server", "file server", "application server", "database server", "backup server", "monitoring agent", "logging server","iSCSI Target", "Hypervisor management", "container orchestration", "cloud sync endpoint"]),
            "isActive": True,
            "devices": [d["_id"]],
            "createdAt": now_ts(),
            "updatedAt": now_ts()
        }
        ips.append(ip_entry)
        if iterCounter % 10000 == 0:
            print(f"  Created {iterCounter} ip addresses so far...")
        iterCounter += 1

print(f"Generated {len(ips)} IP entries.")
print("Data generation complete. Starting database upsert...")

# --- Database insertion (upsert by _id) ---
client = MongoClient(MONGO_CONN)
db = client[DB_NAME]

collections = {
    COL_USERS: users,
    COL_SITES: sites,
    COL_DEVICES: devices,
    COL_CABLES: cables,
    COL_IPS: ips
}

result_counts = {}
for coll_name, docs in collections.items():
    coll = db[coll_name]
    processed = 0
    for doc in docs:
        # Upsert based on _id; if doc has ObjectId it will align with your TS/Mongoose ObjectId refs
        coll.replace_one({'_id': doc['_id']}, doc, upsert=True)
        processed += 1
    result_counts[coll_name] = processed

summary = {
    "database": DB_NAME,
    "counts_upserted": result_counts
}
print(summary)
print("Database upsert complete.")
