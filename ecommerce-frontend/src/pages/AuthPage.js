import axios from "axios";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

// this
function AuthPage() {
    const [isSignup, setIsSignup] = useState(true);
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [phoneNo, setPhoneNumber] = useState('');
    const [selectedPhoneNo, setSelectedPhoneNo] = useState('');
    const [phoneNumbers, setPhoneNumbers] = useState([]); // State for phone numbers
    const navigate = useNavigate();

    useEffect(() => {
        async function fetchPhoneNumbers() {
            try {
                const response = await axios.get("http://localhost:8080/api/ecommerce/users/phones");
                setPhoneNumbers(response.data); // Assuming response.data is an array of phone numbers
            } catch (err) {
                console.error("Failed to fetch phone numbers", err);
            }
        }
        fetchPhoneNumbers();
    }, []);

    async function handleSignup(event) {
        event.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/api/ecommerce/users/", {
                name,
                email,
                phoneNo
            });

            const { userId, phoneNo: registeredPhoneNo } = response.data;
            alert("User Registration Successful");

            navigate('/main-menu', { state: { selectedPhoneNo: registeredPhoneNo, userId } });
        } catch (err) {
            alert("User Registration Failed", err);
        }
    }

    async function handleLogin(event) {
        event.preventDefault();
        try {
            const response = await axios.get(`http://localhost:8080/api/ecommerce/users/phone/${selectedPhoneNo}`);
            const { userId } = response.data;

            navigate('/main-menu', { state: { selectedPhoneNo, userId } });
        } catch (err) {
            alert("Login Failed. Please select a phone number");
        }
    }

    return (
        <div className="container mt-4">
            <div className="btn-group" role="group">
                <button 
                    type="button" 
                    className={`btn ${isSignup ? 'btn-primary' : 'btn-secondary'}`}
                    onClick={() => setIsSignup(true)}
                >
                    Sign Up
                </button>
                <button 
                    type="button" 
                    className={`btn ${!isSignup ? 'btn-primary' : 'btn-secondary'}`}
                    onClick={() => setIsSignup(false)}
                >
                    Login
                </button>
            </div>

            <div className="form-box mt-4">
                {isSignup ? (
                    <form onSubmit={handleSignup}>
                        <div className="form-group">
                            <label>Name</label>
                            <input 
                                type="text" 
                                className="form-control"  
                                placeholder="Enter Name"
                                value={name}
                                onChange={(event) => setName(event.target.value)}
                            />
                        </div>

                        <div className="form-group">
                            <label>Email</label>
                            <input 
                                type="email" 
                                className="form-control"  
                                placeholder="Enter Email"
                                value={email}
                                onChange={(event) => setEmail(event.target.value)}
                            />
                        </div>

                        <div className="form-group">
                            <label>Phone Number</label>
                            <input 
                                type="text" 
                                className="form-control"  
                                placeholder="Enter Phone Number"
                                value={phoneNo}
                                onChange={(event) => setPhoneNumber(event.target.value)}
                            />
                        </div>

                        <button type="submit" className="btn btn-primary mt-4">Register</button>
                    </form>
                ) : (
                    <form onSubmit={handleLogin}>
                        <div className="form-group">
                            <label>Phone Number</label>
                            <p style={{ whiteSpace: 'pre-line' }}></p>
                            <select
                                className="form-control"
                                value={selectedPhoneNo}
                                onChange={(event) => setSelectedPhoneNo(event.target.value)}
                            >
                                <option value="">Select Phone Number</option>
                                {phoneNumbers.map((phone, index) => (
                                    <option key={index} value={phone}>
                                        {phone}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <button type="submit" className="btn btn-primary mt-4">Login</button>
                    </form>
                )}
            </div>
        </div>
    );
}

export default AuthPage;
