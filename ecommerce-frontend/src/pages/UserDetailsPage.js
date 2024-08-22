import axios from "axios";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { useNavigate, useLocation } from "react-router-dom";


function UserDetailsPage() {

    const { phoneNo } = useParams();
    const [user, setUser] = useState(null);
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [updatedPhoneNo, setUpdatedPhoneNo] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);  
    const location = useLocation();
    const { userId } = location.state || {};  
    const navigate = useNavigate();


    console.log("User ID:", userId);
    console.log("phone number", updatedPhoneNo);

    useEffect(() => {
        async function fetchUserDetails() {
            try {
                const response = await axios.get(`http://localhost:8080/api/ecommerce/users/phone/${phoneNo}`);
                setUser(response.data);
                setName(response.data.name);
                setEmail(response.data.email);
                setUpdatedPhoneNo(response.data.phoneNo);
                setLoading(false);
            } catch (err) {
                setError("Failed to fetch user details");
                setLoading(false);
            }
        }
        fetchUserDetails();
    }, [phoneNo]);

    const saveUserDetails = async () => {
        try {
            await axios.put(`http://localhost:8080/api/ecommerce/users/${user.userId}`, {
                name,
                email,
                phoneNo: updatedPhoneNo
            });
            alert("User details updated successfully");
        } catch (err) {
            alert("Failed to update user details");
        }
    };

    const goToMainMenu = () => {
        navigate('/main-menu', { state: { selectedPhoneNo: updatedPhoneNo, userId } });
    };

    const deleteUser = async () => {
        const confirmDelete = window.confirm("Are you sure you want to delete your account?");
        if (confirmDelete) {
            try {
                await axios.delete(`http://localhost:8080/api/ecommerce/users/${user.userId}`);
                alert("Account removed successfully");
                navigate("/auth");
            } catch (err) {
                alert("Failed to delete user");
            }
        }
    };

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div className="container mt-4">
            <h2>User Details</h2>
            {user && (
                <>
                    <div className="form-box">
                        <label>User ID</label>
                        <input type="text" className="form-control" value={user.userId} readOnly />
                    </div>
                    <div className="form-box">
                        <label>Name</label>
                        <input type="text" className="form-control" value={name} onChange={(e) => setName(e.target.value)} />
                    </div>
                    <div className="form-box">
                        <label>Email</label>
                        <input type="email" className="form-control" value={email} onChange={(e) => setEmail(e.target.value)} />
                    </div>
                    <div className="form-box">
                        <label>Phone Number</label>
                        <input type="text" className="form-control" value={updatedPhoneNo} onChange={(e) => setUpdatedPhoneNo(e.target.value)} />
                    </div>
                    <div>
                    <button className="btn btn-primary mt-4" onClick={saveUserDetails}>Save</button>
                    <button className="btn btn-danger mt-4 ml-2" onClick={deleteUser}>Delete Account</button>
                    </div>
                    <button className="btn btn-dark mt-4" onClick={goToMainMenu}>Back</button>

                </>
            )}
        </div>
    );
}

export default UserDetailsPage;
