export default function RiskBadge({ nivel }) {
  const map = {
    Bajo:      "bg-emerald-900/30 text-emerald-200 border-emerald-800/50",
    Medio:     "bg-amber-900/30 text-amber-200 border-amber-800/50",
    Moderado:  "bg-orange-900/30 text-orange-200 border-orange-800/50",
    Alto:      "bg-red-900/35 text-red-200 border-red-800/60",
  };
  const cls = map[nivel] ?? "bg-slate-800 text-slate-200 border-slate-700/50";
  return (
    <small className={`inline-block rounded border px-2 py-0.5 text-xs font-medium ${cls}`}>
      {nivel}
    </small>
  );
}
