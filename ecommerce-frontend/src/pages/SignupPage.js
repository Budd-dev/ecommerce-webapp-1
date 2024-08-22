import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

function SignupPage() {

    const [id, setUserId] = useState('');
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [phoneNo, setPhoneNumber] = useState('');
    const navigate = useNavigate();  // Use navigate from react-router-dom

    async function save(event) {
        event.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/api/ecommerce/users/", {
                name: name,
                email: email,
                phoneNo: phoneNo
            });
            
            // Assuming the response contains the newly created user's ID and phone number
            const { userId, phoneNo: registeredPhoneNo } = response.data;

            alert("User Registration Successfully");
            
            // Clear form fields
            setUserId("");
            setName("");
            setEmail("");
            setPhoneNumber("");

            // Navigate to Main Menu with state
            navigate('/main-menu', { state: { selectedPhoneNo: registeredPhoneNo, userId } });

        } catch (err) {
            alert("User Registration Failed");
        }
    }

    return (
        <div className="container mt-4"> 
            <form>
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

                <button className="btn btn-primary mt-4" onClick={save}>Register</button>
            </form> 
        </div>
    );
}

export default SignupPage;
