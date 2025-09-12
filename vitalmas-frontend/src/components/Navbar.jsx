import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <header className="sticky top-0 z-10 border-b border-slate-800/70 bg-slate-950/60 backdrop-blur">
      <nav className="mx-auto flex h-14 max-w-7xl items-center justify-between px-4">
        <Link to="/" className="text-lg font-semibold tracking-tight">
          Vitalmas
        </Link>
        <div className="flex items-center gap-6 text-sm">
          <Link to="/" className="text-slate-300 hover:text-white">Inicio</Link>
          <Link to="/login" className="text-slate-300 hover:text-white">Ingresar</Link>
          <span className="text-xs text-slate-400 hidden sm:inline">No autenticado</span>
        </div>
      </nav>
    </header>
  );
}
