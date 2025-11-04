import { Link, useLocation, useNavigate } from 'react-router-dom';

// Gắn className trực tiếp vào JSX
export function Navbar({token, setToken}: {token: string | null; setToken: (token: string | null) => void}) {

    const navigate = useNavigate(); //điều khiển
    const location = useLocation() //để dò xem đang ở trang nào

    const isLoggedIn = token != null; //true nếu đã đăng nhập
    //true nếu đang ở trang Login hoặc Register
    const isAuthPage = location.pathname === '/login' || location.pathname === '/register';

    //đăng xuất
    const handleLogout = () => {
        localStorage.removeItem("token"); //vứt token khỏi localStorage
        setToken(null); //báo cho App.tsx biết
        navigate("/"); //chuyển về trang chủ
    }

    // (Không cần các const style ở đây nữa)

    return (
        // Gắn className cho nav
        <nav className="navbar">
            {/*logo bên trái (luôn hiện)*/}
            <div className="navbar-logo">
                <Link to="/">
                    {/* Thêm className cho logo để CSS nhận */}
                    <img src="/logo.png" alt="Yuno Logo" className="logo-image" />
                </Link>
            </div>

            {/*links bên phải (hiển thị có điều kiện)*/}
            <div className="navbar-links">
                <ul>
                    {/*nếu ở trạng thái Auth*/}
                    {isAuthPage && (<></>)}

                    {/*nếu ko ở trạng thái Auth và đã đăng nhập*/}
                    {!isAuthPage && isLoggedIn && (
                        <li><button onClick={handleLogout} className="nav-button">Đăng xuất</button></li>
                    )}
                    {/*nếu ko ở trạng thái Auth và chưa đăng nhập*/}
                    {!isAuthPage && !isLoggedIn &&(
                        <>
                            <li><Link to="/login" className="nav-link-item">Đăng nhập</Link></li>
                            <li><Link to="/register" className="nav-link-item">Đăng ký</Link></li>
                        </>
                    )}
                </ul>
            </div>
        </nav>
    );
}