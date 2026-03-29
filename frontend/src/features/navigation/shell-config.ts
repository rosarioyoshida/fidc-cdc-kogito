export type NavigationItem = {
  label: string;
  href: string;
  exact?: boolean;
};

export type SecondaryNavigationConfig = {
  items: NavigationItem[];
};

export const primaryNavigation: NavigationItem[] = [
  {
    label: "Cessoes",
    href: "/cessoes",
    exact: false
  }
];

export function buildPrimaryAction() {
  return {
    label: "Nova cessao",
    href: "/cessoes#nova-cessao"
  };
}

export function buildCessoesSecondaryNavigation(): SecondaryNavigationConfig {
  return {
    items: [
      {
        label: "Lista",
        href: "/cessoes",
        exact: true
      }
    ]
  };
}

export function buildCessaoContextNavigation(businessKey: string): SecondaryNavigationConfig {
  return {
    items: [
      {
        label: "Resumo",
        href: `/cessoes/${businessKey}`,
        exact: true
      },
      {
        label: "Analise",
        href: `/cessoes/${businessKey}/analise`
      },
      {
        label: "Auditoria",
        href: `/cessoes/${businessKey}/auditoria`
      }
    ]
  };
}
