{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  buildInputs = [
    pkgs.jdk21
    pkgs.maven
  ];

  shellHook = ''
    export PATH="${pkgs.maven}/bin:$PATH"
  '';
}
