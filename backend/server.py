from flask import Flask, request, jsonify, send_file
import datetime
import math
import secrets
import io

app = Flask(__name__)

# --- In-Memory Database ---
users = {
    "test@example.com": {"_id": "user123", "name": "Manager John", "role": "MANAGER", "franchiseId": "fran123", "password": "password"}
}
franchises = {
    "fran123": {"_id": "fran123", "name": "Delhi Outlet 1", "royaltyPercentage": 10, "coordinates": {"latitude": 28.7041, "longitude": 77.1025, "radius": 500}}
}
sales = []
attendance = []
inventory = []
disputes = []
ai_scans = []

# --- Helpers ---
def get_user_from_token(request):
    # Mock validation: Token is just "Bearer <email>"
    auth_header = request.headers.get('Authorization')
    if not auth_header: return None
    email = auth_header.split(" ")[1] if len(auth_header.split(" ")) > 1 else ""
    # For demo simplicity, accept any token as "test@example.com" if not found
    if email in users: return users[email]
    return users["test@example.com"]

def distance(lat1, lon1, lat2, lon2):
    return math.sqrt((lat2-lat1)**2 + (lon2-lon1)**2) * 111000 # Approx meters

# --- Routes ---

@app.route('/', methods=['GET'])
def home():
    return "AI Franchise System (Python Backend) is Running!"

# 1. Auth
@app.route('/api/users/login', methods=['POST'])
def login():
    data = request.json
    email = data.get('email')
    # Mock login - accept any password
    if email in users:
        return jsonify({
            "_id": users[email]["_id"],
            "name": users[email]["name"],
            "email": email,
            "role": users[email]["role"],
            "franchiseId": users[email]["franchiseId"],
            "token": email # Use email as mock token
        })
    # Create user on fly for demo
    return jsonify({
        "_id": "new" + secrets.token_hex(4),
        "name": "New User",
        "email": email,
        "role": "MANAGER",
        "franchiseId": "fran123",
        "token": email 
    })

# 2. Sales
@app.route('/api/sales', methods=['POST', 'GET'])
def handle_sales():
    user = get_user_from_token(request)
    if not user: return jsonify({"message": "Unauthorized"}), 401
    
    if request.method == 'POST':
        # Multipart handled differently, but let's assume JSON for metadata or simulate
        # If Android sends Multipart, Flask handles it via request.form and request.files
        amount = request.form.get('amount') or request.json.get('amount')
        sales.append({
            "franchiseId": user['franchiseId'],
            "amount": float(amount),
            "createdAt": datetime.datetime.now()
        })
        return jsonify({"message": "Sale Recorded", "verified": True}), 201
    
    return jsonify(sales)

# 3. Attendance
@app.route('/api/attendance/checkin', methods=['POST'])
def checkin():
    user = get_user_from_token(request)
    if not user: return jsonify({"message": "Unauthorized"}), 401
    attendance.append({
        "userId": user['_id'],
        "type": "CHECKIN",
        "time": datetime.datetime.now()
    })
    return jsonify({"message": "Check-in Successful"}), 201

# 4. Inventory
@app.route('/api/inventory/log', methods=['POST'])
def inventory_log():
    log = request.json
    inventory.append(log)
    return jsonify({"message": "Inventory Logged"}), 201

@app.route('/api/inventory/items', methods=['GET'])
def get_items():
    return jsonify([
        {"_id": "item1", "name": "Coffee Beans", "unit": "kg", "currentStock": 50, "minStockThreshold": 10},
        {"_id": "item2", "name": "Milk", "unit": "liters", "currentStock": 20, "minStockThreshold": 5}
    ])

# 5. Reports (Royalty)
@app.route('/api/reports/royalty', methods=['GET'])
def royalty():
    total_sales = sum([s['amount'] for s in sales])
    royalty_amt = total_sales * 0.10
    return jsonify({
        "period": "Current",
        "data": [{
            "franchiseName": "Delhi Outlet 1",
            "totalSales": total_sales,
            "royaltyPercentage": 10,
            "calculatedRoyalty": royalty_amt
        }]
    })

# 6. AI & Dashboard
@app.route('/api/ai/analyze', methods=['POST'])
def run_ai():
    # Logic: If recent sales > 0, healthy
    score = {
        "franchiseId": "fran123",
        "healthScore": 95 if len(sales) > 0 else 50,
        "fraudScore": 0,
        "failureRisk": 5,
        "anomalies": [],
        "recommendations": ["Keep up the good work"]
    }
    ai_scans.append(score)
    return jsonify(score)

@app.route('/api/ai/insights/<fid>', methods=['GET'])
def get_insights(fid):
    if ai_scans: return jsonify(ai_scans[-1])
    return jsonify({
        "franchiseId": fid,
        "healthScore": 88,
        "fraudScore": 2,
        "failureRisk": 10,
        "recommendations": ["System Initialized - No Abnormalities"]
    })

@app.route('/api/dashboard/manager/<fid>', methods=['GET'])
def manager_dashboard(fid):
    return jsonify({
        "salesTrend": [], # Simplified
        "complianceStatus": [],
        "healthScore": 88,
        "fraudRisk": 2
    })

# 7. Governance
@app.route('/api/governance/dispute', methods=['POST'])
def dispute():
    disputes.append(request.json)
    return jsonify({"message": "Dispute Created"}), 201

@app.route('/api/reports/audit/<fid>/download', methods=['GET'])
def download_audit(fid):
    # Determine the absolute path to the audit report file
    import os
    file_path = os.path.abspath("audit_report.csv")
    
    # Create the dummy CSV file
    with open(file_path, "w") as f:
        f.write("Audit Report,Passed,2026\n")
        
    return send_file(file_path, as_attachment=True, download_name=f"audit_{fid}.csv")

if __name__ == '__main__':
    print("Starting Python Backend on Port 5000...")
    app.run(host='0.0.0.0', port=5000)
