import axios from "axios";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";


function LoginPage() {
    const [phoneNumbers, setPhoneNumbers] = useState([]);
    const [selectedPhoneNo, setSelectedPhoneNo] = useState('');
    const [userId, setUserId] = useState(null);
    const navigate = useNavigate();
    

    useEffect(() => {
        async function fetchPhoneNumbers() {
            const response = await axios.get("http://localhost:8080/api/ecommerce/users/phones");
            setPhoneNumbers(response.data);
        }
        fetchPhoneNumbers();
    }, []);  
    
    useEffect(() => {
        if (selectedPhoneNo) {
            async function fetchUserId() {
                try {
                    const response = await axios.get(`http://localhost:8080/api/ecommerce/users/phone/${selectedPhoneNo}`);
                    setUserId(response.data.userId); 
                } catch (error) {
                    console.error("Error fetching userId:", error);
                    setUserId(null);
                }
            }
            fetchUserId();
        }
    }, [selectedPhoneNo]);
    

    const login = () => {
        if (selectedPhoneNo) {
            navigate('/main-menu', { state: { selectedPhoneNo, userId } });
        } else {
            alert("Please select a phone number");
        }
    };

    return (
        <div className="container mt-4">
            <h2>Login</h2>
            <div className="form-group">
                <label>Select Phone Number</label>
                <select
                    className="form-control"
                    value={selectedPhoneNo}
                    onChange={(event) => setSelectedPhoneNo(event.target.value)}
                >
                    <option value="">Select</option>
                    {phoneNumbers.map(phone => (
                        <option key={phone} value={phone}>{phone}</option>
                    ))}
                </select>
            </div>
            <button className="btn btn-primary mt-4" onClick={login}>Login</button>
        </div>
    );
}

export default LoginPage;
