export default function Footer() {
  const year = new Date().getFullYear()
  return (
    <footer className="py-3 text-center text-xs text-slate-400" aria-label="Pie de página">
      © {year} Vitalmas · Todos los derechos reservados.
    </footer>
  )
}
