import { Link, useLocation } from 'react-router-dom';
import { IoLogOutOutline } from 'react-icons/io5';

const LogOut = IoLogOutOutline as React.ElementType;

interface AuthUser{
    id: number;
    name: string;
    username: string;
}

interface NavBarProps {
    authUser: AuthUser | null; //nhận User hoặc null
    onLogout: () => void; //nhận hàm đăng xuất
}
// Gắn className trực tiếp vào JSX
export function Navbar({authUser, onLogout}: NavBarProps) {

    const location = useLocation() //để dò xem đang ở trang nào

    const isLoggedIn = authUser != null; //true nếu đã đăng nhập
    //true nếu đang ở trang Login hoặc Register
    const isAuthPage = location.pathname === '/login' || location.pathname === '/register';

    // (Không cần các const style ở đây nữa)

    return (
        // Gắn className cho nav
        <nav className="navbar">
            {/*logo bên trái (luôn hiện)*/}
            <div className="navbar-logo">
                <Link to="/">
                    {/* Thêm className cho logo để CSS nhận */}
                    <img src="/logoExpand.png" alt="Yuno Logo" className="logo-image" />
                </Link>
            </div>

            {/*links bên phải (hiển thị có điều kiện)*/}
            <div className="navbar-links">
                <ul>
                    {/*nếu ở trạng thái Auth*/}
                    {isAuthPage && (<></>)}

                    {/*nếu ko ở trạng thái Auth và đã đăng nhập*/}
                    {!isAuthPage && isLoggedIn && (
                        <>
                            <li className="nav-username">{authUser.name}</li>
                            <li>
                                <button onClick={onLogout} className="nav-button" title="Đăng xuất">
                                    <LogOut size={25} />
                                </button>
                            </li>
                        </>
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