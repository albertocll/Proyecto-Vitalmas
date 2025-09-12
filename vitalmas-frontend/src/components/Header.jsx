import { Link, NavLink } from "react-router-dom";

export default function Header({ displayName, onLogout }) {
  const linkCls = ({ isActive }) =>
    `text-sm transition ${
      isActive
        ? "text-cyan-400 font-semibold underline"
        : "text-slate-300 hover:text-cyan-300"
    }`;

  return (
    <header className="sticky top-0 z-10 border-b border-slate-800/70 bg-slate-950/60 backdrop-blur">
      <div className="mx-auto flex max-w-7xl items-center justify-between px-4 py-2.5">
        {/* Logo / Brand */}
        <Link
          to="/inicio"
          aria-label="Ir al inicio"
          className="group flex select-none items-center gap-2"
        >
          <img
            src="/brand/vitalmas-icon.png"
            alt="Vitalmas"
            className="h-10 w-10 md:h-12 md:w-12"
            loading="eager"
          />
          <span className="hidden sm:inline font-semibold text-slate-200 group-hover:text-cyan-300 text-lg md:text-xl tracking-tight">
            Vitalmas
          </span>
        </Link>

        {/* Estado + Nav */}
        <div className="flex items-center gap-6">
          <div className="flex items-center gap-2 text-xs text-slate-400">
            {displayName ? (
              <>
                Conectado: <b className="text-slate-200">{displayName}</b>
                <button
                  onClick={onLogout}
                  className="cursor-pointer rounded border border-slate-600 bg-transparent px-2 py-0.5 text-slate-300 hover:bg-slate-800"
                >
                  Salir
                </button>
              </>
            ) : (
              "No autenticado"
            )}
          </div>

          <nav className="flex gap-3" aria-label="Navegación principal">
            <NavLink to="/inicio" className={linkCls}>
              Inicio
            </NavLink>
            {!displayName && (
              <NavLink to="/login" className={linkCls}>
                Ingresar
              </NavLink>
            )}
          </nav>
        </div>
      </div>
    </header>
  );
}
