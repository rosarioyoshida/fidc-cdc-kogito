import { redirect } from "next/navigation";
import { LoginPanel } from "@/features/security/login-panel";
import { getCurrentUserOptional, signInAction } from "@/features/security/actions";

type HomePageProps = {
  searchParams?: Promise<Record<string, string | string[] | undefined>>;
};

export default async function HomePage({ searchParams }: HomePageProps) {
  const params = (await searchParams) ?? {};
  const user = await getCurrentUserOptional();
  if (user) {
    redirect("/cessoes");
  }

  const errorMessage =
    typeof params.error === "string" ? decodeURIComponent(params.error) : undefined;
  const lastUsername =
    typeof params.username === "string" ? decodeURIComponent(params.username) : undefined;

  return <LoginPanel errorMessage={errorMessage} lastUsername={lastUsername} signInAction={signInAction} />;
}
