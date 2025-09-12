import clsx from "clsx";

export default function Button({
  children,
  variant = "primary",
  as: Component = "button",
  type = "button",
  className,
  ...props
}) {
  const base =
    "inline-flex items-center justify-center rounded-lg px-4 py-2 text-sm font-medium focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2";

  const variants = {
    primary:
      "bg-cyan-500 text-slate-900 hover:bg-cyan-400 focus-visible:ring-cyan-400 focus-visible:ring-offset-slate-900",
    outline:
      "border border-slate-600 bg-transparent text-slate-200 hover:bg-slate-800 focus-visible:ring-slate-400 focus-visible:ring-offset-slate-900",
  };

  return (
    <Component
      type={Component === "button" ? type : undefined}
      className={clsx(base, variants[variant], className)}
      {...props}
    >
      {children}
    </Component>
  );
}
