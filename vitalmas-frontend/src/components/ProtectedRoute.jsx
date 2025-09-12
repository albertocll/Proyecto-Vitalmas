import { Navigate, useLocation } from 'react-router-dom'

export default function ProtectedRoute({ children }) {
  // auth pobre pero suficiente
  const auth = (() => {
    try { return JSON.parse(sessionStorage.getItem('auth') || 'null') } catch { return null }
  })()
  const isAuthed = !!auth?.usuario?.usuario || !!auth?.usuario?.nombre
  const location = useLocation()

  if (!isAuthed) {
    // guarda a dónde quería ir para volver tras login
    return <Navigate to="/login" replace state={{ from: location }} />
  }
  return children
}
